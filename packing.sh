#!/bin/bash

. /etc/profile

# 这个参数标明当前机器是不是开发机，如果是开发机器，打包时会将所整个工程复制一份到其他路径去打包，打包完成之后会将文件放回原工程对应路径
# 0不是，1是
declare -i DEVELOPMENT=1
# android sdk路径，会在当前环境中搜索，查找失败脚本就会停止执行
SDK_ROOT="UNKNOWN"
# gradle中定义的buildToolsVersion，会在工程根目录的build.gradle中查找，非app/build.gradle，查找失败脚本就会停止执行
BUILD_TOOL="UNKNOWN"
# 打包好之后文件的输出目录，这里是本地目录，如果为空文件会在默认目录下，即 ./app/build/output/apk/，这里是自行定义的，想打包之后的apk输出到特定目录可以在这里定义
OUTPUT_DIR="UNKNOWN"

# 在这里定义重命名，如果找不到定义会保留原来的名字，如果目前的flavor有变动，在这里修改
debug="测试版"
release="正式版"
cargo="货主版"
ship="船主版"
truck="车主版"
dev="开发"
busi="业务"
product="生产"
# 重命名结束

# APK签名信息，Release版本使用，如果不定义会跳过签名步骤，可能会导致apk无法安装
# 签名必须的几个元素keystore，keystore-pasword，key-alias，key-alias-password
# 命名规范-ks对应keystore，-ksp对应keystore-pasword，-ka对应key-alias，-kap对应key-alias-password
# 由于不同的dimension最终会组合在一起，故用一个维度定义签名就够了
# **************** truck签名 ****************
truck_ks="apkSign/truck/android.keystore"
truck_ksp="hlet@201608@0831"
truck_ka="android"
truck_kap="hlet@201608@0831"
# **************** ship签名 ****************
ship_ks="apkSign/ship/android.keystore"
ship_ksp="pass@hlet201609"
ship_ka="android"
ship_kap="pass@hlet201609"
# **************** cargo签名 ****************
cargo_ks="apkSign/huo/android.keystore"
cargo_ksp="pass@hlet201609"
cargo_ka="android"
cargo_kap="pass@hlet201609"

# 说明，这个数组用来存储所有的flavors和它对应的dimension，shell不支持多维数组，而且数组内容没有类型限制
# 具体的存储方法：
# 数组的以一个元素是 dimension的个数n
# 第 2~2*n+1个元素内容是 dimension的名字以及dimension的flavor个数
# 后面的元素就是具体的flavor
# 根据目前的情况举例，目前是dimension：app_type，env，app_type 对应 cargo truck ship 3个flavor，env对应dev busi product
# 数组就应该是这样子
# 2，app_type，3，env，3，cargo，truck，ship，dev，busi，product
DIMENSIONS=()
# 具体要构建的任务，比如assembleCargoDevDebug,...
ASSEMBLE_TASK=()
# 生成的apk的名字，这个数组和ASSEMBLE_TASK里面的内容是一一对应的，比如assembleCargoDevDebug，对应的就是 cargo-dev-debug
APK_NAME=()

declare -i exit_flag=0

# 接受用户输入，并判断参数是否合法
# 参数1 可输入范围的起点
# 参数2 可输入范围的终点
function readNumber(){
  read input
  # 判断输入是不是数字
  if [[ $((input)) != $input ]]; then
    echo -1
  else
    # 判断输入的数字是否在范围之内
    if test $input -le $2; then
      if test $input -ge $1; then
        echo $input
        return 0
      fi
    fi
    echo -1
  fi
}

#将文件移动到指定位置,OPD参数为目的地
# 这里会以./app/build/outputs/apk为目录进行查找，查找失败则不作任何操作
# 参数1 要移动的apk名字，例如 app-cargo-dev-debug.apk
function relocate() {
  if test -d $OUTPUT_DIR; then
    if [ -w $OUTPUT_DIR ]; then
      name=./app/build/outputs/apk/$1
      if test -f $name; then
        mv $name $OUTPUT_DIR
        return 0
      else
        echo "can not find apk file $1 in app/build/outputs/apk"
      fi
    else
      echo "dir $OUTPUT_DIR is not writable in current session"
    fi
  fi
  return 1
}

#文件重命名
# 源文件名，不包含路径和后缀
function rename() {
  IFS='-' read -ra names <<< "$1"
  translate=""
  for (( i = 0; i < ${#names[@]}; i++ )); do
    var=${names[$i]}
    tr=$(echo "${!var}")
    if [ ! -z "${tr// }" ]; then
      if [[ $i -eq 0 ]]; then
        translate=$tr
      else
        translate=$translate\_$tr
      fi
    else
      if [[ $i -eq 0 ]]; then
        translate=$var
      else
        translate=$translate\_$var
      fi
    fi
  done
  echo "$translate"
}

# 打release包需要用到zip命令
if [ ! -f $(which zip) ]; then
  echo "zip is not installed, exit..."
  exit 1
fi

#navigate to script dir
cd $(dirname ${BASH_SOURCE[0]})
# 工程的路径
PROJECT_DIR=$(pwd)
# 把相对路径的签名配置补全为绝对路径
if [[ ! $truck_ks == /* ]]; then
  truck_ks=$PROJECT_DIR/$truck_ks
fi
if [[ ! $ship_ks == /* ]]; then
  ship_ks=$PROJECT_DIR/$ship_ks
fi
if [[ ! $cargo_ks == /* ]]; then
  cargo_ks=$PROJECT_DIR/$cargo_ks
fi

# 打包的路径
PACKING_DIR=$(dirname $PROJECT_DIR)/project_COPY_for_PACKING/
# 删除可能存在的缓存
rm -rf $PACKING_DIR
# 如果是开发机，就在别的路径打包
if test $DEVELOPMENT -eq 1; then
  echo "development is set true, packing will be carried out in copy project"
  echo "start copying project files"
  cp -a $PROJECT_DIR/. $PACKING_DIR
  echo "copy finished, redirect to $PACKING_DIR"
  cd $PACKING_DIR
fi

#test if this is project root dir
if test -d app/src/main/java/com/hletong/hyc -a -d hletonglib; then
  echo "searching android sdk"
else
  echo "put this script under project root dir then run it again"
  exit 1
fi
#是否要重建local.properties文件，如果local.properties里面定义的sdk.dir参数不能指向正确的sdk路径，打包的时候会失败，这时候就需要重建local.properties文件，这种情况通常是在服务器打包
REBUILD_LOCAL_PROPERTIES=0
if test -f local.properties; then
  while IFS=' ' read -r line || [[ -n "$line" ]]; do
    if [[ $line == sdk.dir=* ]]; then
      IFS='=' read -ra SDK <<< "$line"
      SDK_ROOT=${SDK[1]}
      if test -d $SDK_ROOT/build-tools -a -f $SDK_ROOT/platform-tools/adb -a -d $SDK_ROOT/platforms; then
        echo "find sdk in local.properties: $SDK_ROOT"
      else
        SDK_ROOT="UNKNOWN"
      fi
    fi
  done < local.properties
fi

if [[ $SDK_ROOT == "UNKNOWN" ]]; then
  #要重新生成local.properties文件
  REBUILD_LOCAL_PROPERTIES=1
  echo "failed to find android sdk in local.properties,search ANDROID_HOME"
  env_dir=$(echo $ANDROID_HOME)
  if [ ! -z "${param// }" ]; then
    SDK_ROOT=$env_dir
    echo "find sdk in ANDROID_HOME: $SDK_ROOT"
  else
    echo "failed to find android sdk in ANDROID_HOME,search for adb"
    ADB_DIR=$(which adb)
    if test ! -z $ADB_DIR -a -f $ADB_DIR; then
      SDK_ROOT=${ADB_DIR:0:$(expr ${#ADB_DIR} - 18)}
      echo "find sdk in adb path: $SDK_ROOT"
    else
      SDK_ROOT="UNKNOWN"
      echo "failed to locate sdk,please add android home to path variable,exit..."
    fi
  fi
fi

# 查找android sdk失败
if [[ $SDK_ROOT == "UNKNOWN" ]]; then
  exit 1
fi

#检测签名文件是否存在
if ! test -f $truck_ks -a -f $ship_ks -a -f $cargo_ks ; then
  echo "sign config is not valid,exit..."
  exit 1
fi

# find buildToolVersion
while IFS=' ' read -r line || [[ -n "$line" ]]; do
  if [[ "$line" == *buildToolsVersion* ]]; then
    BUILD_TOOL=${line//buildToolsVersion:}
    BUILD_TOOL=${BUILD_TOOL//\'}
    BUILD_TOOL=${BUILD_TOOL//\,}
    BUILD_TOOL=${BUILD_TOOL// }
    version=$(echo -e "$BUILD_TOOL" | tr -d '[:space:]')
    SDK_ROOT=${SDK_ROOT%%/}

    BUILD_TOOL=$SDK_ROOT/build-tools/$BUILD_TOOL
    BUILD_TOOL=$(echo -e "$BUILD_TOOL" | tr -d '[:space:]')
    if ! test -d $BUILD_TOOL; then
      echo "failed to locate build tools $version in sdk dir,exit..."
      exit_flag=1
    else
      if ! test -f $BUILD_TOOL/zipalign; then
        echo "failed to locate zipalign in build tools $version,exit..."
        exit_flag=1
      else
        if ! test -f $BUILD_TOOL/apksigner; then
          echo "failed to locate apksigner in build tools $version,exit..."
          exit_flag=1
        fi
      fi
    fi
    break
  fi
done < build.gradle

if test $exit_flag -eq 1; then
  exit 1
fi

declare -i brackets=0
tmp=''
while IFS=' ' read -r line || [[ -n "$line" ]]; do
  if [[ $line == flavorDimensions* ]]; then
    index=0
    flavors_array=${line:16}
    IFS=',' read -ra flavors <<< "$flavors_array"
    DIMENSIONS[0]=${#flavors[@]}
    # echo "dimsion length ${DIMENSIONS[0]},${#flavors[@]}"
    for (( i = 0; i < DIMENSIONS[0]; i++ )); do
      start=$(expr $(expr $i \* 2) + 1)
      # dimension的名字
      DIMENSIONS[$start]=$(echo -e "${flavors[$i]}" | tr -d '[:space:]')
      # dimenson对应的flavor个数，暂赋值为0
      DIMENSIONS[$(expr $start + 1)]=0
    done
  elif [[ $line == productFlavors* ]]; then
    # 进入productFlavors查找flavor以及其对应的dimension
    brackets=1
  elif [[ $brackets -gt 0 ]]; then
    p="${line//[^\{]}"
    m="${line//[^\}]}"
    brackets_new=$(expr $brackets + ${#p} - ${#m})
    if [[ $brackets_new -ne $brackets ]]; then
      if test $brackets_new -eq 2  -a $brackets -eq 1; then
        IFS='\{' read -ra fl <<< "$line"
        tmp+=${fl[0]}
        # echo "$tmp"
      fi
      if test $brackets_new -eq 1  -a $brackets -eq 2; then
        tmp=''
      fi
      brackets=$brackets_new
      # echo "current level $brackets"
    fi
    if [[ $brackets -eq 2 ]]; then
      if [[ $line == dimension* ]]; then
        IFS=' ' read -ra dimension <<< "$line"
        flv=$(echo -e "${dimension[1]}" | tr -d '[:space:]')
        # 这个元素插入的位置
        insert_index=$(expr $(expr ${DIMENSIONS[0]} \* 2) + 1)
        for (( i = 0; i < ${#DIMENSIONS[@]}; i++ )); do
          if test $i -gt 0 -a $(expr $i % 2) -eq 0; then
            insert_index=$(expr $insert_index + ${DIMENSIONS[$i]})
          fi
          # echo "test ${DIMENSIONS[$i]} and $flv"
          if [ "${DIMENSIONS[$i]}" = "$flv" ]; then
            # 找到了对应的dimension
            insert_index=$(expr $insert_index + ${DIMENSIONS[$(expr $i + 1)]})
            DIMENSIONS[$(expr $i + 1)]=$(expr ${DIMENSIONS[$(expr $i + 1)]} + 1)
            if test $insert_index -lt ${#DIMENSIONS[@]}; then
              tmp_value=''

              # 数组向后移位
              end=${#DIMENSIONS[@]}
              for (( i = $insert_index; i < $end; i++ )); do
                tmp_value=${DIMENSIONS[$insert_index]}
                DIMENSIONS[$insert_index]=${DIMENSIONS[$(expr $i + 1)]}
                DIMENSIONS[$(expr $i + 1)]=$tmp_value
              done
            fi
            # 插入具体的flavor
            tmp="${tmp//[\}]}"
            tmp=$(echo -e "$tmp" | tr -d '[:space:]')
            DIMENSIONS[$insert_index]=$tmp
            break
          fi
        done
      fi
    elif [[ $brackets -eq 1 ]]; then
      IFS='\/\/' read -ra flavors <<< "$line"
      tmp+=${flavors[0]}
    fi
  fi
done < app/build.gradle
#
# echo "***************************************"
declare -i previous_flavor_count=0
declare -i dimension_index=0

for (( i = 1; i <= $(expr ${DIMENSIONS[0]} \* 2); i++ )); do
  name=""
  current_input=-1
  if test $(expr $i % 2) -eq 0 ; then
    index=1;
    start=$(expr $(expr ${DIMENSIONS[0]} \* 2) + 1)
    start=$(expr $start + $previous_flavor_count)
    end=$(expr $start + ${DIMENSIONS[$i]})

    if [[ $# -eq 3 ]]; then
      #脚本带了参数就不用输入了
      index=$(expr $i / 2 - 1)
      IFS=' ' read -ra params <<< "$*"
      if [[ ${params[$index]} == "all" ]]; then
        current_input=0
      else
        for (( j = $start; j < $end; j++ )); do
          if [[ ${params[$index]} == "${DIMENSIONS[$j]}" ]]; then
            current_input=$(expr $j - $start + 1)
            break
          fi
        done
      fi
    else
      # 脚本未带参数或者参数个数不正确，这种情况让用户输入
      echo "choose a flavor for dimension: ${DIMENSIONS[$(expr $i - 1)]}, input 0 will build all flavors in this dimension"
      for (( j = $start; j < $end; j++ )); do
        echo "$index.${DIMENSIONS[$j]}"
        index=$(expr $index + 1)
      done

      current_input=$(readNumber 0 ${DIMENSIONS[$i]})
      if test $current_input -eq -1; then
        echo "number must be in [0,${DIMENSIONS[$i]}],please type again"
        current_input=$(readNumber 0 ${DIMENSIONS[$i]})
      fi
    fi

    if test $current_input -eq -1; then
      echo "invalid flavor type, exit... "
      exit_flag=1
      break
    else
      # 把当前选择的内容放到任务数组里面
      AS2=()
      # 同上
      AS3=()
      # 将当前的任务复制到AS2中
      for (( q = 0; q < ${#ASSEMBLE_TASK[@]}; q++ )); do
        AS2[$q]=${ASSEMBLE_TASK[$q]}
        AS3[$q]=${APK_NAME[$q]}
      done

      length=$(expr $end - $start)
      # 这是第一个dimension
      if [[ ${#AS2[@]} -eq 0 ]]; then
        # 选择了所有
        if [[ $current_input -eq 0 ]]; then
          for (( j = $start; j < $end; j++ )); do
            cf=${DIMENSIONS[$j]}
            uf="$(tr '[:lower:]' '[:upper:]' <<< ${cf:0:1})${cf:1}"
            ii=${#ASSEMBLE_TASK[@]}
            ASSEMBLE_TASK[$ii]=$uf
            APK_NAME[$ii]=$cf
            # echo "task $ii ${ASSEMBLE_TASK[$ii]}"
          done
        else
          # 只选择了一个
          cf=${DIMENSIONS[$(expr $(expr $start + $current_input) - 1)]}
          APK_NAME[$p]=$cf
          ASSEMBLE_TASK[$p]="$(tr '[:lower:]' '[:upper:]' <<< ${cf:0:1})${cf:1}"
          # echo "task $p ${ASSEMBLE_TASK[$p]}"
        fi
      else
        # 不是第一个dimension
        ni=0
        for (( p = 0; p < ${#AS2[@]}; p++ )); do
          if [[ $current_input -eq 0 ]]; then
            # 选择了所有
            for (( j = $start; j < $end; j++ )); do
              cf=${DIMENSIONS[$j]}
              uf="$(tr '[:lower:]' '[:upper:]' <<< ${cf:0:1})${cf:1}"
              ASSEMBLE_TASK[$ni]=${AS2[$p]}$uf
              APK_NAME[$ni]=${AS3[$p]}-$cf
              # echo "task $ni ${ASSEMBLE_TASK[$ni]}"
              ni=$(expr $ni + 1)
            done
          else
            # 选择了其中一个
            cf=${DIMENSIONS[$(expr $(expr $start + $current_input) - 1)]}
            uf="$(tr '[:lower:]' '[:upper:]' <<< ${cf:0:1})${cf:1}"
            ASSEMBLE_TASK[$p]=${AS2[$p]}$uf
            APK_NAME[$p]=${AS3[$p]}-$cf
            # echo "task $p ${ASSEMBLE_TASK[$p]}"
          fi
        done
      fi
    fi

    previous_flavor_count=$(expr $previous_flavor_count + ${DIMENSIONS[$i]})
    dimension_index=$(expr $dimension_index + 1)
  fi
done

if test $exit_flag -eq 1; then
  exit 1
fi

if [[ $# -eq 3 ]]; then
  if test $3 == "debug" -o $3 == "Debug"; then
    buildType=1
  fi
  if test $3 == "release" -o $3 == "Release"; then
    buildType=2
  fi
else
  echo "choose a build type:"
  echo "1.Debug"
  echo "2.Release"

  buildType=$(readNumber 1 2)
  if test $buildType -eq -1; then
    echo "invalid build type, please input again:"
    buildType=$(readNumber 1 2)
  fi
fi

if test $buildType -eq -1; then
  echo "invalid build type, exit... "
  exit 1
fi

if [ ! -f gradlew ]; then
  echo "gradlew file is not exist"
  exit 1
fi

if [ ! -x gradlew ]; then
  chmod +x gradlew
  if [ ! -x gradlew ]; then
    echo "failed to make gradlew executable, please add execute permission to gradlew manually then run this script again"
    exit 1
  fi
fi

if [[ $REBUILD_LOCAL_PROPERTIES -eq 1 ]]; then
  #创建新的local.properties文件并写入sdk路径
  mv local.properties local.properties.bck
  touch local.properties
  echo "sdk.dir=$SDK_ROOT" >> local.properties
fi

#替换build.gradle文件中的签名信息
signScope=0
signInfo=""
mv build.gradle build.gradle.bck
touch build.gradle

while IFS=' ' read -r line || [[ -n "$line" ]]; do
  if [[ "$line" == truckSignConfig* ]]; then
    signScope=1
    signInfo="truckSignConfig"
  fi
  if [[ "$line" == shipSignConfig* ]]; then
    signScope=1
    signInfo="shipSignConfig"
  fi
  if [[ $signScope -eq 1 ]]; then
    if [[ "$line" == *\] ]]; then
      signScope=0
      echo "$signInfo = [keyAlias : '$truck_ka',keyPassword:'$truck_kap',storeFile:'$truck_ks',storePassword:'$truck_ksp']" >> build.gradle
    fi
  else
    echo "$line" >> build.gradle
  fi
done < build.gradle.bck

# 正式打包之前，做一下清理工作
./gradlew clean

# 正式开始打包
if [[ $buildType -eq 1 ]]; then
  # debug版本
  for (( i = 0; i < ${#ASSEMBLE_TASK[@]}; i++ )); do
    APK_NAME[$i]=${APK_NAME[$i]}-debug
    ./gradlew $(echo "assemble${ASSEMBLE_TASK[$i]}Debug")
    # 打包生成的文件的名字
    original_name=app-${APK_NAME[$i]}.apk
    # 经过命名转换的名字
    translate_name=Android_$(rename ${APK_NAME[$i]})\_$(date '+%m%d%H').apk

    # 文件重命名
    mv app/build/outputs/apk/$original_name app/build/outputs/apk/$translate_name
    # 判断输出文件夹是否存在，是否可写入
    if test -d $OUTPUT_DIR -a -w $OUTPUT_DIR; then
      mv app/build/outputs/apk/$translate_name $OUTPUT_DIR
    fi
  done
else
  # release版本，这里需要手动签名
  for (( i = 0; i < ${#ASSEMBLE_TASK[@]}; i++ )); do
    # 任务名
    taskName=$(echo "assemble${ASSEMBLE_TASK[$i]}Release")
    ./gradlew $taskName
    # 数组里面的原始数据
    simple_name=${APK_NAME[$i]}
    # 打包生成的文件的名字
    original_name=app-$simple_name-release.apk
    # zipalign对其之后的文件名
    aligned_name=app-$simple_name-aligned.apk
    # 签名之后的名字
    signed_name=app-$simple_name-signed.apk
    # 命名转换之后的名字
    translated_name=Android_$(rename $simple_name)\_$(date '+%m%d%H').apk
    echo "original_name $original_name"
    echo "aligned_name $aligned_name"
    echo "signed_name $signed_name"
    echo "translated_name $translated_name"

    # 查找自定义签名，删除默认签名，使用自定义的签名配置来进行签名
    if ! test -f app/build/outputs/apk/$original_name; then
      echo "failed to find packed apk named $original_name in dir app/build/outputs/apk/, abort tasks"
      exit_flag=1
      break
    fi
    # 删除默认签名
    zip -d app/build/outputs/apk/$original_name META-INF/\*
    # zipalign
    $BUILD_TOOL/zipalign -v -f -p 4 app/build/outputs/apk/$original_name app/build/outputs/apk/$aligned_name
    rm -f app/build/outputs/apk/$original_name

    declare -i succeed=0
    # apk签名
    IFS='-' read -ra flavors <<< "$simple_name"
    for (( j = 0; j < ${#flavors[@]}; j++ )); do
      var=${flavors[$j]}_ks
      ks=$(echo "${!var}")
      ks=${ks// }
      # echo "flavor ${flavors[$j]} ks config => $ks"
      # 签名参数校验
      if [ ! -z "$ks" ]; then
        if test -f $ks; then
          var=${flavors[$j]}_ksp
          ksp=$(echo "${!var}")
          ksp=${ksp// }

          var=${flavors[$j]}_ka
          ka=$(echo "${!var}")
          ka=${ka// }

          var=${flavors[$j]}_kap
          kap=$(echo "${!var}")
          kap=${kap// }

          echo "flavor ${flavors[$j]} ksp config => $ksp"
          echo "flavor ${flavors[$j]} ka config => $ka"
          echo "flavor ${flavors[$j]} kap config => $kap"

          if [ ! -z "$ksp" -a ! -z "$ka" -a ! -z "$kap" ]; then
            succeed=1
            # 别换行
            $BUILD_TOOL/apksigner sign --out app/build/outputs/apk/$signed_name --v1-signing-enabled true --v2-signing-enabled true --ks $ks --ks-key-alias $ka --ks-pass pass:$ksp --key-pass pass:$kap app/build/outputs/apk/$aligned_name
            rm -f app/build/outputs/apk/$aligned_name
            $BUILD_TOOL/apksigner verify app/build/outputs/apk/$signed_name

            # 文件重命名
            if ! test -f app/build/outputs/apk/$signed_name; then
              echo "failed to find signed apk $signed_name in app/build/outputs/apk/, abort tasks"
              exit_flag=1
              break
            fi

            mv app/build/outputs/apk/$signed_name app/build/outputs/apk/$translated_name
            # 判断输出文件夹是否存在，是否可写入
            if test -d $OUTPUT_DIR -a -w $OUTPUT_DIR; then
              mv app/build/outputs/apk/$translated_name $OUTPUT_DIR
            fi
            # echo "apk was signed, break"
            break
          fi
        fi
      fi
    done

    if test $succeed -eq 0; then
      if ! test $exit_flag -eq 1; then
        echo "failed to sign apk, check your signature config above and make sure params are ok, exit...."
        exit_flag=1
      fi
      break
    fi
  done
fi

#文件还原
if test -f local.properties.bck; then
  rm -f local.properties
  mv local.properties.bck local.properties
fi

#文件还原
if test -f build.gradle.bck ; then
  rm -f build.gradle
  mv build.gradle.bck build.gradle
fi

if test $exit_flag -eq 1; then
  exit 1
fi

# 如果是开发机，打包完之后要把数据回写到源工程路径的 ./auto_pack_outputs 目录
if test $DEVELOPMENT -eq 1 ; then
  rm -rf $PROJECT_DIR/auto_pack_outputs/
  cp -a app/build/outputs/. $PROJECT_DIR/auto_pack_outputs/
  rm -rf $PACKING_DIR
  echo "packing outputs is in $PROJECT_DIR/auto_pack_outputs/"
fi

echo "*******************************************"
if test -d $OUTPUT_DIR -a -w $OUTPUT_DIR; then
  echo "apk file(s) dir : $OUTPUT_DIR"
else
  echo "apk file(s) dir : app/build/outputs/apk/"
fi
echo "*******************************************"
echo "**************** have fun *****************"
echo "*******************************************"
