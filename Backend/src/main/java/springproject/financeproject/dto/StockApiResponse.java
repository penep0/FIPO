package springproject.financeproject.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
public class StockApiResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;

        @Data
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }

        @Data
        public static class Body {
            private int numOfRows;
            private int pageNo;
            private int totalCount;
            private Items items;

            @Data
            public static class Items {
                private List<Item> item;

                @Data
                public static class Item {
                    private String basDt;
                    private String srtnCd;
                    private String isinCd;
                    private String itmsNm;
                    private String mrktCtg;
                    private String clpr;
                    private String vs;
                    private String fltRt;
                    private String mkp;
                    private String hipr;
                    private String lopr;
                    private String trqu;
                    private String trPrc;
                    private String lstgStCnt;
                    private String mrktTotAmt;
                }
            }
        }
    }
}