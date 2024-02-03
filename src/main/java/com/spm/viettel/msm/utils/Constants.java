/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spm.viettel.msm.utils;

/**
 *
 * @author kientt
 */
public class Constants {

    public static final int MAX_ROW_PARAM = 1000;
    public static final String TOTAL = "Total";

    public static String templateLocation = "/sqls";

    /**
     * Tham so cho biet so gia tri cua mot nhom lon hon bao nhieu thi se hien
     * thi thanh grid trong view dong
     */
    public static final int REQUIRE_GRID = 3;
    /**
     * linhnt28 so ban ghi max, start khi query
     */
    public static final int MAX_ROW_QUERY = 5000;
    public static final String _AND_ROWNUM__20_ = "";
    public static final String VT_UNIT = "2";
    public static final int PAGE_SIZE = 10;
    public static final int PAGE_SIZE_EXPORT = 100;
    public static final int STATUS_DELETE = -1;
    public static final int constantStartVal = 0;
    public static final int SALE_ASSIGN_BR = 1;
    public static final int STATUS_DRAFT = 2;
    public static final int BR_SUBMIT = 3 ;
    public static final int SALE_APPROVED = 4 ;
    public static final int SALE_REJECTED = 5 ;
    public static final int BC_ASSIGN_AC_AF = 6 ;
    public static final int AC_AF_PLAN = 7 ;
    public static final int BC_APPROVED = 8 ;
    public static final int BC_REJECTED = 9 ;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_REJECTED = -2;
    public static final int STATUS_DESTROY = -3;
    public static final long VTP_SHOP_ID = 7282l;
    public static final String SALE_POLICY_FOR_BTS_NORMAL_NAME = "Bts Normal";
    public static final String ACTIVE_STATUS_NAME = "Active";
    public static final String INACTIVE_STATUS_NAME = "Inactive";
    public static final int VISIT_PLAN_RESULT_REJECT = -1;
    public static final int VISIT_PLAN_RESULT_APPROVED = 1;
    /**
     * linhnt28 constants for export excel
     */
    public static final int constantRowInsertHeader = 1;
    public static final int constantRowInsertTitle = 5;
    public static final int constantRowInsertData = 6;
    public static final String CONTRACT_METHOD_0 = "N/A";
    public static final String CONTRACT_METHOD_1 = "Có hợp đồng ủy quyền";
    public static final String CONTRACT_METHOD_2 = "Có ký biên bản thỏa thuận";
    public static final String CONTRACT_METHOD_3 = "Không ký thỏa thuận";
    public static final String CONSTANT_CONTRACT_METHOD_0 = "0";
    public static final String CONSTANT_CONTRACT_METHOD_1 = "1";
    public static final String CONSTANT_CONTRACT_METHOD_2 = "2";
    public static final String CONSTANT_CONTRACT_METHOD_3 = "3";
    
    public static final String DB_DEFAULT = "Default Session";
    public static final String DB_ANYPAY = "AnypaySession";
    public static final String DB_SM = "SM_Session";
    public static final String DB_BONUS = "BonusSession";

    public static final String SHOP_TYPE_AGENT = "AGENT";
    public static final String SHOP_TYPE_BC = "BC";
    public static final String SHOP_TYPE_BR = "BR";
    public static final String SHOP_TYPE_VTP = "VTP";

    /**
     * linhnt28 channelTypeId truong hop nhan vien co toa do
     */
    public static final int CHANNEL_TYPE_ID_STAFF = 14;
    public static final long CHANNEL_TYPE_ID_DF = 101052599L;
    /**
     * linhnt28 channelTypeId truong hop diem ban co toa do
     */
    public static final int CHANNEL_TYPE_ID_SELLING_POINT = 10;
    /**
     * HuyPQ15 ChannelTypeId là đại lý
     */
    public static final int CHANNEL_TYPE_ID_AGENT = 4;
    /**
     * HuyPQ15 ChannelTypeId là cửa hàng
     */
    public static final int CHANNEL_TYPE_ID_SHOP = 1;
    public static final long CHANNEL_TYPE_ID_POS = 80043;
    public static final long CHANNEL_TYPE_ID_D2D = 10;
    public static final long CHANNEL_TYPE_ID_DL = 4;
    public static final long CHANNEL_TYPE_ID_CD = 101052639;
    public static final long CHANNEL_TYPE_ID_SD = 101052799;
    /**
     * HuyPQ15 Xuat du lieu cho hoat dong cua truong Truong trung tam huyen
     */
    public static final int CHANNEL_TYPE_ID_STAFF_ACTIVITY = 141;
    public static final int CHANNEL_TYPE_ID_MANAGER_ACTIVITY = 142;
    public static final String BUSINESS = "Business";
    public static final String PROJECT_NAME = "MSM";
    public static final String DEFAULT = "DEFAULT";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String REPORT_OBJECT = "objReportObject";
    public static final String USER_TOKEN = "userToken"; //
    public static final String USER_PROFILE = "userProfile";
    public static final String IS_EXPORT_EXCEL = "isExportExcel";
    public static final String IS_EXPORT_HTML = "isExportHTML";
    public static final String EXCEL_STREAM = "excelStream";
    public static final String SESSION_TIMEOUT = "sessionTimeout";
    public static final String FIST_VALUE = "-1";
    public static final Long FILE_MAX_SIZE = 10485760L;
    public static final int PROPERTY_VALUE_MAX_LENGTH = 1000;
    public static final String FILE_PERMIT_UP = "rar,zip,jpg,bmp,avi,png,jpeg,RAR,ZIP,JPG,BMP,AVI,PNG,JPEG";
    public static final long ACTIVE = 1;
    public static final int ACTIVE_INT = 1;
    public static final long INACTIVE = 0;
    public static final long SEARCH_ITEM_CONFIG = 1;
    public static final long DOWNLOAD_SEARCH_ITEM_CONFIG = 2;
    public static final String UPLOAD_PATH = "/share/upload/";
    
    /**
     * Ten file config gui mail
     */
    public static final String MAIL_FILE_CONFIG = "mail";
    /**
     * Host smtp dung gui email
     */
    public static final String MAIL_HOST = "mail.smtp.host";
    /**
     * Port dung gui mail
     */
    public static final String MAIL_PORT = "mail.smtp.port";
    /**
     * Attribute gui mail
     */
    public static final String MAIL_ATTR = "mail.smtp.starttls.enable";
    /**
     * Auth Thuoc tinh dang nhap
     */
    public static final String MAIL_AUTH = "mail.smtp.auth";
    /**
     * Dia chi mail gui
     */
    public static final String MAIL_ADDRESS = "mail.address";
    /**
     * Pass hom thu
     */
    public static final String MAIL_PASS = "mail.pass";
    public static final Long CANCELED = 5L;
    public static final Long COMPLETED = 4L;
    public static final Long ASSIGNED = 2L;
    public static final Long REGISTER = 8L;
    public static final Long WAIT_APPROVER = 7L;
    //Start thanhlq6
    public static final Long HEADER_TYPE = 0L;
    public static final Long CONTENT_TYPE = 1L;
    public static final long SALE_POLICY_FOR_BTS_NORMAL = 5l;
    //End thanhlq6

    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String MM_dd_YYYY = "MM/dd/yyyy";

    public static final class ROLE {

        public static final String VT = "VT";// cap VT
        public static final String CNKD = "CN";// cap CN
        public static final String SHOP = "CH";//cap CH
    }

    public static final class FUNC_TYPE {

        public static final String ALL = "ALL";// tim tất cả
        public static final String NO_XY = "NOXY";// không có tọa độ
        public static final String NO_OWNER = "NOOWNER";//không có NV quản lý
    }

    public static final class SMS_CONFIG {

        public static final String SMS_CONFIG_CODE = "SMS_CONFIG";
        public static final String SMS_CONFIG_CODE_VAS = "SMS_CONFIG_VAS";
        public static final String SMS_CONFIG_ULR = "URL";
        public static final String SMS_CONFIG_XMLNS = "XMLNS";
        public static final String SMS_CONFIG_USERNAME = "USERNAME";
        public static final String SMS_CONFIG_PASSWORD = "PASSWORD";
        public static final String SMS_CONFIG_COUNTRY_CODE = "COUNTRY_CODE";
        public static final String SMS_CONFIG_SENDER = "SENDER";
        public static final String SMS_CONFIG_SERVICE_ID = "SERVICE_ID";
    }

    public static final class SEQUENCE {

        public static final String CHANGE_REQUEST_SEQ = "CHANGE_REQUEST_SEQ";
        public static final String ACTION_LOG_SEQ = "ACTION_LOG_SEQ";
        public static final String TASK_SEQ = "TASK_SEQ";
        public static final String TASK_STAFF_ID_SEQ = "TASK_STAFF_ID_SEQ";
        public static final String TASK_ROAD_ID_SEQ = "TASK_ROAD_ID_SEQ";
        public static final String STAFF_ASSET_DETAIL_SEQ = "STAFF_ASSET_DETAIL_SEQ";
    }

    public static final class PARAM_TYPE {

        public static final String STAFF_LEVEL = "STAFF_LEVEL";
        //public static final String PRIORITY_TYPE = "PRIORITY";
        public static final String TASK_STATUS = "TASK_STATUS";
        public static final String TASK_CYCLE = "CYCLE";
        public static final String OBJECT_GROUP = "OBJECT_GROUP";
        public static final String TASK_SCOPE = "SCOPE";
        public static final String STAFF_TYPE = "STAFF_TYPE";
        public static final String PRIORITY_TYPE = "PRIORITY_TYPE";
        public static final String TASK_PURPOSE = "TASK_PURPOSE";
    }

    public static final class SCRIPT_JSP {
        //<editor-fold defaultstate="collapsed" desc="function JSP">

        public static final String FUNCTION_MANAGE_PHOTO = "page.ManagePhoto()";
        public static final String FUNCTION_MANAGEBANNER = "page.ManageBanner()";
        public static final String FUNCTION_MANAGEEQUIPPED = "page.ManageEquipped()";
        public static final String FUNCTION_OTHERUNITS = "page.OtherUnits()";
        public static final String FUNCTION_ASSIGN_TARGET = "page.AssignTarget()";
        public static final String FUNCTION_UPDATE = "page.Update()";
        public static final String FUNCTION_SHOW_DETAIL = "page.showDetailInfoFromMap()";
        public static final String FUNCTION_ASSIGNE_TASK = "page.assignTask()";
        public static final String FUNCTION_VIEW_STOCK = "page.viewStock()";
        public static final String FUNCTION_VIEW_STAFF_OWNER = "page.viewStaffOwner()";
        public static final String FUNCTION_ASSIGNE_TARGET = "page.assignTarget()";
        public static final String FUNCTION_VIEW_VISIT = "page.searchVisitDetail()";
        public static final String FUNCTION_VIEW_PAYMENT_INFO = "page.viewPamentInfo()";
        public static final String FUNCTION_VIEW_STAFF_ZONE = "page.viewStaffZone()";
        public static final String FUNCTION_VIEW_STAFF_ROAD = "viewStaffRoad()";
        public static final String FUNCTION_VIEW_SALE_INFO = "page.viewSaleInfo()";
        public static final String FUNCTION_VIEW_ONLINE = "page.viewOnlineLBS()";
        public static final String FUNCTION_TRANS_INFO = "page.viewTransInfo()";
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="acction for grid of MapView">
        public static final String ACCTION_PROGRESS_PAYMENT_INFO = "mapAction!getProgressPaymentInfo.do";
        public static final String ACCTION_3_LAST_SALE_TRANS_INFO = "mapAction!onGet3LastSaleTransDetail.do";
        public static final String ACCTION_3_LAST_STOCK_TRANS_INFO = "mapAction!onGet3LastStockTransDetail.do";
        public static final String ACCTION_VIEW_STOCK_INFO = "mapAction!getStockDetailInfo.do";
        public static final String ACCTION_GET_RETAIL_DETAIL = "mapAction!getRetailDetail.do";
        public static final String ACCTION_GET_SELLOFF_DETAIL = "mapAction!getSellOffDetail.do";
        public static final String ACCTION_GET_OBJECT_DETAIL = "mapAction!getObjectDetail.do";
        //</editor-fold>
//        public static final String CHANNEL_TYPE = "<sd:Property>basicInfo.channelType</sd:Property>";
//        public static final String CODE = "<sd:Property>basicInfo.code</sd:Property>";
//        public static final String NAME = "<sd:Property>basicInfo.name</sd:Property>";
//        public static final String TEL = "<sd:Property>basicInfo.tel</sd:Property>\"";
//        public static final String ADDRESS = "<sd:Property>basicInfo.address</sd:Property>";
//        public static final String STAFF_OWNER = "<sd:Property>basicInfo.staffOwner</sd:Property>";
//        
//        public static final String DETAIL_LABEL = "<sd:Property>basicInfo.detailLabel</sd:Property>";
//        public static final String TARGET_LABEL = "<sd:Property>basicInfo.targetLabel</sd:Property>";
//        public static final String TASK_LABEL = "<sd:Property>basicInfo.taskLabel</sd:Property>";
//        public static final String STOCK_LABEL = "<sd:Property>basicInfo.stockLabel</sd:Property>";
        public static final String CHANNEL_TYPE = "Loại kênh: ";
        public static final String CODE = "Mã: ";
        public static final String NAME = "Tên: ";
        public static final String TEL = "basicInfo.tel";
        public static final String ADDRESS = "basicInfo.address";
        public static final String BTSLIST = "basicInfo.btsList";
        public static final String STAFF_OWNER = "basicInfo.staffOwner";
        public static final String DETAIL_LABEL = "basicInfo.detailLabel";
        public static final String TRANS_LABEL = "basicInfo.transLabel";
        public static final String TARGET_LABEL = "basicInfo.targetLabel";
        public static final String TASK_LABEL = "basicInfo.taskLabel";
        public static final String STOCK_LABEL = "basicInfo.stockLabel";
        public static final String VISIT_LABEL = "basicInfo.visitLabel";
        public static final String VIEW_PAYMENT_INFO_LABEL = "basicInfo.VIEW_PAYMENT_INFO_LABEL";
        public static final String VIEW_STAFF_ROAD_LABEL = "basicInfo.VIEW_STAFF_ROAD_LABEL";
        public static final String VIEW_SALE_INFO_LABEL = "basicInfo.VIEW_SALE_INFO_LABEL";
        public static final String VIEW_STAFF_ZONE_LABEL = "basicInfo.VIEW_STAFF_ZONE_LABEL";
        public static final String VIEW_ONLINE = "basicInfo.VIEW_ONLINE";
        public static final String CHANNEL_TYPE_BTS = "Trạm BTS";
        public static final String CHANNEL_TYPE_SHOP = "Cửa hàng";
        public static final String CHANNEL_TYPE_AGENT = "Đại lý";
        public static final String CHANNEL_TYPE_BHLD = "Bán hàng lưu động";
        public static final String CHANNEL_TYPE_COLLABORATOR = "Điểm bán";
        public static final String CHANNEL_TYPE_REGISTER_INFO = "Điểm đăng ký thông tin";
        public static final String CHANNEL_TYPE_STAFF = "Nhân viên";
        
        //hieunv31\
         public static final String CHANNEL_TYPE_DGDX = "Điểm giao dịch xã";
         public static final String TASK_LABEL_KH = "basicInfo.taskLabel_KH";
         public static final String TASK_LABEL_MH = "basicInfo.taskLabel_MH";
        //end
    }
    public static final String ACTION = "ACTION";
    public static final class USER {

        public static final String CE_DIRECTOR = "CE_DIRECTOR";
        public static final String BR_DIRECTOR = "BR_DIRECTOR";
        public static final String BR_SUB_DIRECTOR = "BR_SUB_DIRECTOR";
        public static final String BR_ASISTENTE_FIJO = "BR_ASISTENTE_FIJO";
        public static final String COLABORADOR_AC = "COLABORADOR_AC";
        public static final String CE_CHANNEL_MANAGER = "CE_CHANNEL_MANAGER";
    }

    public static final class AGENT {

        public static final String CODE = "basicInfo.AGENT.code";
        public static final String NAME = "basicInfo.AGENT.name";
    }

    public static final class BHLD {

        public static final String CODE = "basicInfo.BHLD.code";
        public static final String NAME = "basicInfo.BHLD.name";
        public static final String ADDRESS = "basicInfo.BHLD.address";
    }

    public static final class BTS {

        public static final String CODE = "basicInfo.BTS.code";
        public static final String NAME = "basicInfo.BTS.name";
        public static final String TEL = "basicInfo.BTS.tel";
        public static final String TYPE = "basicInfo.BTS.type";
        public static final String REGISTER_DAY = "basicInfo.BTS.register.day";
        public static final String REGISTER_INCREASE = "basicInfo.BTS.register.increase";
        public static final String POPULATION = "basicInfo.BTS.population";
    }
    public static final class DGDX{
        public static final String CODE = "basicInfo.DGDX.code";
        public static final String NAME = "basicInfo.DGDX.name";
        public static final String TEL = "basicInfo.DGDX.tel";
    }

    public static final class COLLABORATOR {

        public static final String CODE = "basicInfo.COLLABORATOR.code";
        public static final String NAME = "basicInfo.COLLABORATOR.name";
    }

    public static final class STAFF {
        
        public static final String ANYPAY = "basicInfo.STAFF.anypay";
        public static final String CODE = "basicInfo.STAFF.code";
        public static final String NAME = "basicInfo.STAFF.name";
    }

    public static final class REGISTER_INFO {

        public static final String DKTT_USER = "basicInfo.REGISTER_INFO.user";
        public static final String NAME = "basicInfo.REGISTER_INFO.name";
    }

    public static final class LOCATION {

        public static final String CONDITION = "location";
        public static final int NULL = 0;
        public static final int NOT_NULL = 1;
    }

    public static final class STAFF_OWNER {

        public static final String CONDITION = "staffOwner";
        public static final int NULL = 0;
        public static final int NOT_NULL = 1;
    }

    public static final class PARAM_SEARCH {

        public static final String STAFF_ID = "staffID";
        public static final String CHANNEL_TYPE_ID = "channelTypeId";
        public static final String PROVINCE = "province";
        public static final String DISTRICT = "district";
        //HienHV3 them moi
        public static final String PRECINCT = "precinct";
        public static final String REGISTER_INFO = "registerInfo";
        public static final String STAFF_OWNER_ID = "staffOwnerId";
        public static final String STAFF_CODE = "staffCode";
        public static final String OBJECT_TYPE = "objectType";
        public static final String IS_VT_UNIT = "isVtUnit";
    }

    public static final class OBJECT_DETAIL {

        public static final String OBJ_CODE = "objCode";
        public static final String ATT_VALUE = "attValue";
        public static final String ATT_NAME = "att_name";
        public static final String DETAIL_GROUP_NAME = "detailGroupName";
        public static final String PREPAID = "prepaid";
        public static final String OBJ_GROUP_ID = "objGroupId";
    }

    public static final class HTTP_SESSION {

        public static final String TASK = "TASK";
        public static final String LST_TASK_ROAD = "LST_TASK_ROAD";
        public static final String LIST_MAP_OBJECT = "lstObjectMap";
        public static final String LIST_CANDIDATE_OBJECT = "LIST_CANDIDATE_OBJECT";
        public static final String LIST_SELECTED_OBJECT = "LIST_SELECTED_OBJECT";
    }
    //tham so ftp server
    public static final String CONFIG_KEY_FTP_USER_HOST = "FTP_USER_HOST";
    public static final String CONFIG_KEY_FTP_USER_USERNAME = "FTP_USER_USERNAME";
    public static final String CONFIG_KEY_FTP_USER_PASSWORD = "FTP_USER_PASSWORD";
    public static final String CONFIG_KEY_FTP_USER_SERVER_DIR = "FTP_USER_SERVER_DIR";
    public static final String FOLDER_STAFF = "FOLDER_STAFF";
    public static final String FOLDER_SHOP = "FOLDER_SHOP";
    public static final String FOLDER_CUSTOMER = "FOLDER_CUSTOMER";

    /**
     * Ket qua qua trinh validate file va import vao database cua
     */
    public static final class IMP_LOCATION_STATUS {

        /**
         * Truong hop ma kenh bo trong
         */
        public static final String ERROR_CODE_NULL = "ERR01";
        /**
         * Truong hop x rong
         */
        public static final String ERROR_X_NULL = "ERR02";
        /**
         * Truong hop y rong
         */
        public static final String ERROR_Y_NULL = "ERR03";
        /**
         * Truong hop ca ba cot loai doi tuong bi bo trong
         */
        public static final String ERROR_NO_TYPE = "ERR04";
        /**
         * Truong hop mot doi tuong vua la diem DKTT vua la cua hang, dai ly
         */
        public static final String ERROR_BOTH_DKTT_SHOP = "ERR05";
        /**
         * Truong hop mot doi tuong vua la NV,CTV,DB vua la cua hang, dai ly
         */
        public static final String ERROR_BOTH_STAFF_SHOP = "ERR06";
        /**
         * Truong hop x sau khi tach ra lay so khong thoa man
         */
        public static final String ERROR_X_INVALID = "ERR07";
        /**
         * Truong hop y sau khi tach ra lay so khong thoa man
         */
        public static final String ERROR_Y_INVALID = "ERR08";
        /**
         * Truong hop ma kenh ko ton tai
         */
        public static final String ERROR_CODE_UNINVAILABLE = "ERR09";
        /**
         * Truong hop ko co quyen truy cap
         */
        public static final String ERROR_NOT_AUTH = "ERR10";
        /**
         * Truong hop gap exception
         */
        public static final String ERROR_EXCEPTION = "EXCEPTION";
        /**
         * Truong hop thanh cong
         */
        public static final String SUCCESS = "success";
    }
    
    /**
     * Ket qua qua trinh validate file va import vao database cua
     */
    public static final class IMP_VISITPLAN_STATUS {
        /**
         * Truong hop thanh cong
         */
        public static final String SUCCESS = "success";
        public static final String ERROR_ZONAL_EMPTY = "imp.visitplan.status.error.zonal_empty";
        public static final String ERROR_PDV_EMPTY = "imp.visitplan.status.error.pdv_empty";
        public static final String ERROR_DATE_EMPTY = "imp.visitplan.status.error.date_empty";
        public static final String ERROR_DATE_WRONG_FORMART = "imp.visitplan.status.error.date_wrong_format";
        public static final String ERROR_EXCEPTION = "EXCEPTION";
        
    }

    public static final class COLOR {

        public static final String RED = "#B40404";
        public static final String BLACK = "#1C1C1C";
        public static final String VIOLET = "#8A0829";
    }
    /**
     * HuyPQ15: Tham so de lay danh sach cac loai doi tuong truong chuc nang buc
     * tranh chung cua huyen
     */
    public static final String OBJECT_TYPE = "OBJECT_TYPE";
    public static final String COLLECT_OBJECT_TYPE = "COLLECT_VALUE_TYPE";
    //Duong dan den thu muc anh QLHADB
    public static String QLHA_IMG_FOLDER = "/u01/app/xdbd_app/QLDB/uploadFile/";
    public static String QLHA_STAFF_FOLDER = "staffFile/";
    public static String QLHA_SHOP_FOLDER = "shopFile/";
    
//    static {
//        try{
//            QLHA_IMG_FOLDER = ResourceBundleUtils.getResource("QLHA_IMG_FOLDER");
//            QLHA_STAFF_FOLDER = ResourceBundleUtils.getResource("QLHA_STAFF_FOLDER");
//            QLHA_SHOP_FOLDER = ResourceBundleUtils.getResource("QLHA_SHOP_FOLDER");
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
    
    /**
     * HuyPQ15: Loai dai ly
     */
    public static final class AGENT_TYPE {

        public static final String TYPE_4 = "4";
        public static final String TYPE_5 = "5";
        public static final String TYPE_6 = "6";
        public static final String TYPE_7 = "7";
        public static final String TYPE_9 = "9";
    }
    
    public static final int REPORT_LEVEL_PROVINCE = 1;
    public static final int REPORT_LEVEL_DISTRICT = 2;
    public static final int REPORT_LEVEL_PRECINCT = 3;
    public static final int ROW_REFRESH_SESSION = 100;
    public static final String TASK_TYPE_1 = "1";
    public static final String TASK_TYPE_2 = "2";
    public static final String TASK_TYPE_3 = "3";
    public static final String TASK_TYPE_4 = "4";
    public static final String TASK_TYPE_5 = "5";
    public static final int IMPORT_DISTRICT_TEMPLATE_BEGINROW = 7;
    public static final int IMPORT_DISTRICT_TEMPLATE_START_COLUMN = 0;
    public static final String IMPORT_DISTRICT_INSERT_FALSE = "2";
    public static final String IMPORT_DISTRICT_INSERT_SUCCESS = "0";
    public static final String IMPORT_DISTRICT_INVALID_DATA = "1";
    public static final String IMPORT_DISTRICT_AREA_CODE_INVALID = "3";
    
    public static final String CHANNEL_TYPE_SHOP = "SHOP";
    public static final String CHANNEL_TYPE_STAFF = "STAFF";
    public static final String CHANNEL_TYPE_AGENT = "AGENT";
    public static final String CHANNEL_TYPE_COLLABORATOR = "COLLABORATOR";
    
    public static final Integer TYPE_SHOP = 1;
    public static final Integer TYPE_STAFF = 2;
    public static final Integer TARGET_TYPE_DEVELOP_NEW = 1;
    public static final Integer TARGET_TYPE_ACCUMULATED = 2;

    
     public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";
     public static final String MSM_BTS_RIGHT = "MSM_BTS_RIGHT";
     public static final String ERROR = "ERROR";
     public static final String ERROR_CODE = "error_code";
     public static final String LANG = "lang";
     public static final String LANG_EN = "en";
     public static final String LANG_ES = "es";
     public static final String QLKD_MAP_FTP = "QLKD_MAP_FTP";
     public static final String FTP_IS_SSL = "FTP_IS_SSL";
     public static final String FTP_HOST = "FTP_HOST";
     public static final String FTP_USER = "FTP_USER";
     public static final String FTP_PASS = "FTP_PASS";
     public static final String FTP_WORKING_DIR = "FTP_WORKING_DIR";

     public static final String BRANCH_CODE = "Código de sucursal";
     public static final String BC_CODE = "Centro de negocios";
     public static final String STAFF_CODE = "Código de personal";
     public static final String TARGET = "Objetivo";
     public static final String ACCUM = "Acumulado";
     public static final String TARGET_JAN = "Enero";
     public static final String TARGET_FEB = "Febrero";
     public static final String TARGET_MAR = "Marzo";
     public static final String TARGET_APR = "Abril";
     public static final String TARGET_MAY = "Mayo";
     public static final String TARGET_JUN = "Junio";
     public static final String TARGET_JUL = "Julio";
     public static final String TARGET_AUG = "Agosto";
     public static final String TARGET_SEP = "Septiembre";
     public static final String TARGET_OCT = "Octubre";
     public static final String TARGET_NOV = "Noviembre";
     public static final String TARGET_DEC = "Diciembre";
     public static final String TARGET_PDV = "PDV";
     public static final String TARGET_AF = "AF";
     public static final String TARGET_DF = "DF";
     public static final String TARGET_AC = "AC";
     public static final String TARGET_PR = "PR";
     public static final String PREFIX_ALIAS_CHANNEL_TYPE = "C";
     public static final String OBJECT_TYPE_STAFF = "2";

     public static final String CODE_VALIDATION_USING = "ITEM_CONFIG_VALIDATION";
     public static final String CODE_OK_NOK_NA_USING = "ITEM_CONFIG_OK_NOK_NA";
     public static final String CODE_GRAVEDAD_USING = "ITEM_CONFIG_GRAVEDAD";
     public static final String TYPE_ITEM_CONFIG_USING = "IMPORT_ITEM";

     public static final String TYPE_CHANNEL_CHECK_LIST = "CHANNEL_CHECK_LIST";
     public static final String CODE_CHANNEL_CHECK_LIST = "LIST_CHANNEL_TYP";

     public static final String TYPE_PENALTY_USING = "IMPORT_PENALTY";
     public static final String NAME_BR_DIRECTOR =  "DIRECTOR DE SUCURSAL";
     public static final String CODE_PENALTY_PENALIDAD = "PENALTY_PENALIDAD";
     public static final Long STATUS_ACTIVE = 1L;

     public static final Long STATUS_INACTIVE = 0L;
     public static final String TIME_24H_PATTERN = "([01][0-9]|2[0-3]):[0-5][0-9]";

     public static final String VALIDATION_PHOTO = "PHOTO";
     public static final String VALIDATION_FILE = "FILE";
     public static final String VALIDATION_VIDEO = "VIDEO";
     public static final String VALIDATION_AUDIO = "AUDIO";
     public static final String POS_CODE_AF = "BR_ASISTENTE_FIJO";
     public static final String POS_CODE_AC = "COLABORADOR_AC";
     public static final int TARGET_MIN = 0;
    public static final int CHANNEL_TYPE_DF = 101052599; //Asesor fijo channel'
    public static final int CHANNEL_TYPE_PROMOTOR = 10;
    public static final int CHANNEL_TYPE_AF = 14;


}
