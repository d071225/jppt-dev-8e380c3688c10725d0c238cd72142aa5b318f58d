package com.hletong.hyc.model;

import java.io.Serializable;

public  class PaperTable implements Serializable {
        private static final long serialVersionUID = 1488064583705263199L;
        private String paper_type;
        private String paper_file;

        public PaperTable(String paper_type, String paper_file) {
            this.paper_type = paper_type;
            this.paper_file = paper_file;
        }

        public String getPaper_type() {
            return paper_type;
        }

        public void setPaper_type(String paper_type) {
            this.paper_type = paper_type;
        }

        public String getPaper_file() {
            return paper_file;
        }

        public void setPaper_file(String paper_file) {
            this.paper_file = paper_file;
        }

    }