package com.example.msutils;

public class Test {
    public static void main(String[] args) {

        String aaa = "/Users/martinsail/workspace/AmarirsV4.0/AmarIRSV4.0-CDBK/IRS/irs-web/product/RWA/IRS/irs-web/projects/irs-flow-manage/flow-rating-manage/Interbank-rating/src/views/components/common-commit.vue";
        String product = aaa.substring(aaa.indexOf("projects")-1);
        System.out.println(product);
    }
}
