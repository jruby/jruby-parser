package org.jrubyparser.parser;

public class YyTables {
   private static short[] combine(short[] t1, short[] t2, 
                                  short[] t3, short[] t4) {
      short[] t = new short[t1.length + t2.length + t3.length + t4.length];
      int index = 0;
      System.arraycopy(t1, 0, t, index, t1.length);
      index += t1.length;
      System.arraycopy(t2, 0, t, index, t2.length);
      index += t2.length;
      System.arraycopy(t3, 0, t, index, t3.length);
      index += t3.length;
      System.arraycopy(t4, 0, t, index, t4.length);
      return t;
   }

   public static final short[] yyTable() {
      return combine(yyTable1(), yyTable2(), yyTable3(), yyTable4());
   }

   public static final short[] yyCheck() {
      return combine(yyCheck1(), yyCheck2(), yyCheck3(), yyCheck4());
   }
   private static final short[] yyTable1() {
      return new short[] {

      };
   }

   private static final short[] yyTable2() {
      return new short[] {

      };
   }

   private static final short[] yyTable3() {
      return new short[] {

      };
   }

   private static final short[] yyTable4() {
      return new short[] {

      };
   }

   private static final short[] yyCheck1() {
      return new short[] {

      };
   }

   private static final short[] yyCheck2() {
      return new short[] {

      };
   }

   private static final short[] yyCheck3() {
      return new short[] {

      };
   }

   private static final short[] yyCheck4() {
      return new short[] {

      };
   }

}
