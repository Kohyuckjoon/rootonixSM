package com.planuri.rootonixsmartmirror.model;

import java.util.ArrayList;

public class Weather {

    public Response response;

    public static class Response {
        public Header header;
        public Body body;
    }

    public static class Body {
        public String dataType;
        public Items items;
        public int pageNo;
        public int numOfRows;
        public int totalCount;

        /**
         * 기온
         */
        public double T1H;
        /**
         * 하늘상태 :
         * 맑음(1), 구름많음(3), 흐림(4)
         */
        public int SKY;
        /**
         * 강수형태 :
         * 맑음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
         */
        public int PTY = -1;


        /**
         * 기온, 하늘 상태, 강수형태 추출
         */
        public void parseData() {
            for(Item item : items.item) {
                switch (item.category) {
                    case "T1H" :
                        this.T1H = Double.parseDouble(item.obsrValue);
                        break;
                    case "SKY" :
                        this.SKY = Integer.parseInt(item.obsrValue);
                        break;
                    case "PTY" :
                        this.PTY = Integer.parseInt(item.obsrValue);
                        break;
                }
            }
        }

    }

    public static class Items {
        public ArrayList<Item> item;
    }

    public static class Item {
        public String baseDate;
        public String baseTime;
        public String category;
        public String fcstDate;
        public String fcstTime;
        public String fcstValue;
        public String obsrValue;
        public int nx;
        public int ny;

        @Override
        public String toString() {
            return "Item{" +
                    "baseDate='" + baseDate + '\'' +
                    ", baseTime='" + baseTime + '\'' +
                    ", category='" + category + '\'' +
                    ", fcstDate='" + fcstDate + '\'' +
                    ", fcstTime='" + fcstTime + '\'' +
                    ", fcstValue='" + fcstValue + '\'' +
                    ", nx=" + nx +
                    ", ny=" + ny +
                    '}';
        }
    }
}
