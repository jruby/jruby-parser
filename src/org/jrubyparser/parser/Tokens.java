/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2009 Thomas E Enebo <enebo@acm.org>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jrubyparser.parser;

public interface Tokens {
    int yyErrorCode = Ruby18Parser.yyErrorCode;
    int kCLASS      = Ruby18Parser.kCLASS;
    int kMODULE     = Ruby18Parser.kMODULE;
    int kDEF        = Ruby18Parser.kDEF;
    int kUNDEF      = Ruby18Parser.kUNDEF;
    int kBEGIN      = Ruby18Parser.kBEGIN;
    int kRESCUE     = Ruby18Parser.kRESCUE;
    int kENSURE     = Ruby18Parser.kENSURE;
    int kEND        = Ruby18Parser.kEND;
    int kIF         = Ruby18Parser.kIF;
    int kUNLESS     = Ruby18Parser.kUNLESS;
    int kTHEN       = Ruby18Parser.kTHEN;
    int kELSIF      = Ruby18Parser.kELSIF;
    int kELSE       = Ruby18Parser.kELSE;
    int kCASE       = Ruby18Parser.kCASE;
    int kWHEN       = Ruby18Parser.kWHEN;
    int kWHILE      = Ruby18Parser.kWHILE;
    int kUNTIL      = Ruby18Parser.kUNTIL;
    int kFOR        = Ruby18Parser.kFOR;
    int kBREAK      = Ruby18Parser.kBREAK;
    int kNEXT       = Ruby18Parser.kNEXT;
    int kREDO       = Ruby18Parser.kREDO;
    int kRETRY      = Ruby18Parser.kRETRY;
    int kIN         = Ruby18Parser.kIN;
    int kDO         = Ruby18Parser.kDO;
    int kDO_COND    = Ruby18Parser.kDO_COND;
    int kDO_BLOCK   = Ruby18Parser.kDO_BLOCK;
    int kRETURN     = Ruby18Parser.kRETURN;
    int kYIELD      = Ruby18Parser.kYIELD;
    int kSUPER      = Ruby18Parser.kSUPER;
    int kSELF       = Ruby18Parser.kSELF;
    int kNIL        = Ruby18Parser.kNIL;
    int kTRUE       = Ruby18Parser.kTRUE;
    int kFALSE      = Ruby18Parser.kFALSE;
    int kAND        = Ruby18Parser.kAND;
    int kOR         = Ruby18Parser.kOR;
    int kNOT        = Ruby18Parser.kNOT;
    int kIF_MOD     = Ruby18Parser.kIF_MOD;
    int kUNLESS_MOD = Ruby18Parser.kUNLESS_MOD;
    int kWHILE_MOD  = Ruby18Parser.kWHILE_MOD;
    int kUNTIL_MOD  = Ruby18Parser.kUNTIL_MOD;
    int kRESCUE_MOD = Ruby18Parser.kRESCUE_MOD;
    int kALIAS      = Ruby18Parser.kALIAS;
    int kDEFINED    = Ruby18Parser.kDEFINED;
    int klBEGIN     = Ruby18Parser.klBEGIN;
    int klEND       = Ruby18Parser.klEND;
    int k__LINE__   = Ruby18Parser.k__LINE__;
    int k__FILE__   = Ruby18Parser.k__FILE__;
    int k__ENCODING__ = Ruby18Parser.k__ENCODING__;
    int kDO_LAMBDA = Ruby18Parser.kDO_LAMBDA;

    int tIDENTIFIER = Ruby18Parser.tIDENTIFIER;
    int tFID        = Ruby18Parser.tFID;
    int tGVAR       = Ruby18Parser.tGVAR;
    int tIVAR       = Ruby18Parser.tIVAR;
    int tCONSTANT   = Ruby18Parser.tCONSTANT;
    int tCVAR       = Ruby18Parser.tCVAR;
    int tINTEGER    = Ruby18Parser.tINTEGER;
    int tFLOAT      = Ruby18Parser.tFLOAT;
    int tSTRING_CONTENT     = Ruby18Parser.tSTRING_CONTENT;
    int tSTRING_BEG = Ruby18Parser.tSTRING_BEG;
    int tSTRING_END = Ruby18Parser.tSTRING_END;
    int tSTRING_DBEG= Ruby18Parser.tSTRING_DBEG;
    int tSTRING_DVAR= Ruby18Parser.tSTRING_DVAR;
    int tXSTRING_BEG= Ruby18Parser.tXSTRING_BEG;
    int tREGEXP_BEG = Ruby18Parser.tREGEXP_BEG;
    int tREGEXP_END = Ruby18Parser.tREGEXP_END;
    int tWORDS_BEG      = Ruby18Parser.tWORDS_BEG;
    int tQWORDS_BEG      = Ruby18Parser.tQWORDS_BEG;
    int tBACK_REF   = Ruby18Parser.tBACK_REF;
    int tBACK_REF2  = Ruby18Parser.tBACK_REF2;
    int tNTH_REF    = Ruby18Parser.tNTH_REF;

    int tUPLUS      = Ruby18Parser.tUPLUS;
    int tUMINUS     = Ruby18Parser.tUMINUS;
    int tUMINUS_NUM     = Ruby18Parser.tUMINUS_NUM;
    int tPOW        = Ruby18Parser.tPOW;
    int tCMP        = Ruby18Parser.tCMP;
    int tEQ         = Ruby18Parser.tEQ;
    int tEQQ        = Ruby18Parser.tEQQ;
    int tNEQ        = Ruby18Parser.tNEQ;
    int tGEQ        = Ruby18Parser.tGEQ;
    int tLEQ        = Ruby18Parser.tLEQ;
    int tANDOP      = Ruby18Parser.tANDOP;
    int tOROP       = Ruby18Parser.tOROP;
    int tMATCH      = Ruby18Parser.tMATCH;
    int tNMATCH     = Ruby18Parser.tNMATCH;
    int tDOT        = Ruby18Parser.tDOT;
    int tDOT2       = Ruby18Parser.tDOT2;
    int tDOT3       = Ruby18Parser.tDOT3;
    int tAREF       = Ruby18Parser.tAREF;
    int tASET       = Ruby18Parser.tASET;
    int tLSHFT      = Ruby18Parser.tLSHFT;
    int tRSHFT      = Ruby18Parser.tRSHFT;
    int tCOLON2     = Ruby18Parser.tCOLON2;

    int tCOLON3     = Ruby18Parser.tCOLON3;
    int tOP_ASGN    = Ruby18Parser.tOP_ASGN;
    int tASSOC      = Ruby18Parser.tASSOC;
    int tLPAREN     = Ruby18Parser.tLPAREN;
    int tLPAREN2     = Ruby18Parser.tLPAREN2;
    int tRPAREN     = Ruby18Parser.tRPAREN;
    int tLPAREN_ARG = Ruby18Parser.tLPAREN_ARG;
    int tLBRACK     = Ruby18Parser.tLBRACK;
    int tRBRACK     = Ruby18Parser.tRBRACK;
    int tLBRACE     = Ruby18Parser.tLBRACE;
    int tLBRACE_ARG     = Ruby18Parser.tLBRACE_ARG;
    int tSTAR       = Ruby18Parser.tSTAR;
    int tSTAR2      = Ruby18Parser.tSTAR2;
    int tAMPER      = Ruby18Parser.tAMPER;
    int tAMPER2     = Ruby18Parser.tAMPER2;
    int tSYMBEG     = Ruby18Parser.tSYMBEG;
    int tTILDE      = Ruby18Parser.tTILDE;
    int tPERCENT    = Ruby18Parser.tPERCENT;
    int tDIVIDE     = Ruby18Parser.tDIVIDE;
    int tPLUS       = Ruby18Parser.tPLUS;
    int tMINUS       = Ruby18Parser.tMINUS;
    int tLT         = Ruby18Parser.tLT;
    int tGT         = Ruby18Parser.tGT;
    int tCARET      = Ruby18Parser.tCARET;
    int tBANG       = Ruby18Parser.tBANG;
    int tLCURLY     = Ruby18Parser.tLCURLY;
    int tRCURLY     = Ruby18Parser.tRCURLY;
    int tPIPE       = Ruby18Parser.tPIPE;
    int tLAMBDA     = Ruby18Parser.tLAMBDA;
    int tLAMBEG     = Ruby18Parser.tLAMBEG;
    int tLABEL      = Ruby18Parser.tLABEL;
    int tCOMMENT    = Ruby18Parser.tCOMMENT;
    int tWHITESPACE = Ruby18Parser.tWHITESPACE;
    int tDOCUMENTATION = Ruby18Parser.tDOCUMENTATION;
    int tSYMBOLS_BEG = Ruby20Parser.tSYMBOLS_BEG;
    int tQSYMBOLS_BEG = Ruby20Parser.tQSYMBOLS_BEG;
    int tDSTAR = Ruby20Parser.tDSTAR;
    int tIMAGINARY = Ruby20Parser.tIMAGINARY;
    int tRATIONAL = Ruby20Parser.tRATIONAL;
    int tLABEL_END = Ruby23Parser.tLABEL_END;
    int tANDDOT = Ruby23Parser.tANDDOT;    
    int tSTRING = Ruby23Parser.tSTRING;
    
    String[] operators = {"+@", "-@", "**", "<=>", "==", "===", "!=", ">=", "<=", "&&",
                          "||", "=~", "!~", "..", "...", "[]", "[]=", "<<", ">>", "::" };
}
