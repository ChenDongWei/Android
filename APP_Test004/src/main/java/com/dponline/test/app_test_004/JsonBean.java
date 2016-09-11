package com.dponline.test.app_test_004;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/9.
 */
public class JsonBean {
    public Task Task;

    public static class Task {
        public int TaskId;
        public int TaskTypeId;
        public String ReportPeriod;
        public String ReportDate;
        public boolean IsVisitTask;
        public String TaskLetter;
        public String TaskName;
        public String ResultTableName;
        public String Processor;
        public boolean IsEnable;
        public boolean HasPicture;
        public boolean PictureIsRequired;
        public String PictureName;
        public int PictureModeId;
        public boolean HasLocate;
        public boolean LocateIsRequired;
        public String LocateName;
        public boolean HasScanning;
        public boolean ScanningIsRequired;
        public String ScanningName;
        public int RepairDay;
        public int TaskReportTypeId;
        public String TaskReportDemo;
        public int ImmediateResponseId;
        public boolean IsSkipProcessor;
        public String InsertTime;
        public String Operator;
        public boolean IsStandardTask;
        public int OrderNumber;
        public int GroupCount;
        public String GroupTitle;
        public int TaskTemplateId;
        public String PictureRemark1;
        public String PictureRemark2;
        public String PictureRemark3;
        public String LocateRemark1;
        public String LocateRemark2;
        public String LocateRemark3;
        public String ScanningRemark1;
        public String ScanningRemark2;
        public String ScanningRemark3;
        public int OptionalTimeType;
        public String ScanContentFormatRegex;
        public boolean ScanContentMustExist;
        public int ProcessAuthorizationId;

        @Override
        public String toString() {
            return "Task{" +
                    "TaskId=" + TaskId +
                    ", TaskTypeId=" + TaskTypeId +
                    ", ReportPeriod='" + ReportPeriod + '\'' +
                    ", ReportDate='" + ReportDate + '\'' +
                    ", IsVisitTask=" + IsVisitTask +
                    ", TaskLetter='" + TaskLetter + '\'' +
                    ", TaskName='" + TaskName + '\'' +
                    ", ResultTableName='" + ResultTableName + '\'' +
                    ", Processor='" + Processor + '\'' +
                    ", IsEnable=" + IsEnable +
                    ", HasPicture=" + HasPicture +
                    ", PictureIsRequired=" + PictureIsRequired +
                    ", PictureName='" + PictureName + '\'' +
                    ", PictureModeId=" + PictureModeId +
                    ", HasLocate=" + HasLocate +
                    ", LocateIsRequired=" + LocateIsRequired +
                    ", LocateName='" + LocateName + '\'' +
                    ", HasScanning=" + HasScanning +
                    ", ScanningIsRequired=" + ScanningIsRequired +
                    ", ScanningName='" + ScanningName + '\'' +
                    ", RepairDay=" + RepairDay +
                    ", TaskReportTypeId=" + TaskReportTypeId +
                    ", TaskReportDemo='" + TaskReportDemo + '\'' +
                    ", ImmediateResponseId=" + ImmediateResponseId +
                    ", IsSkipProcessor=" + IsSkipProcessor +
                    ", InsertTime='" + InsertTime + '\'' +
                    ", Operator='" + Operator + '\'' +
                    ", IsStandardTask=" + IsStandardTask +
                    ", OrderNumber=" + OrderNumber +
                    ", GroupCount=" + GroupCount +
                    ", GroupTitle='" + GroupTitle + '\'' +
                    ", TaskTemplateId=" + TaskTemplateId +
                    ", PictureRemark1='" + PictureRemark1 + '\'' +
                    ", PictureRemark2='" + PictureRemark2 + '\'' +
                    ", PictureRemark3='" + PictureRemark3 + '\'' +
                    ", LocateRemark1='" + LocateRemark1 + '\'' +
                    ", LocateRemark2='" + LocateRemark2 + '\'' +
                    ", LocateRemark3='" + LocateRemark3 + '\'' +
                    ", ScanningRemark1='" + ScanningRemark1 + '\'' +
                    ", ScanningRemark2='" + ScanningRemark2 + '\'' +
                    ", ScanningRemark3='" + ScanningRemark3 + '\'' +
                    ", OptionalTimeType=" + OptionalTimeType +
                    ", ScanContentFormatRegex='" + ScanContentFormatRegex + '\'' +
                    ", ScanContentMustExist=" + ScanContentMustExist +
                    ", ProcessAuthorizationId=" + ProcessAuthorizationId +
                    '}';
        }
    }

    public ArrayList<tp> TaskParams;

    public class tp{
        public int TaskParamId;
        public int TaskId;
        public String TaskParamName;
        public String IndividualTaskParamName;
        public String TaskParamType;
        public int TaskParamOrder;
        public String ResultTableFieldName;
        public int IndividualMaxLength;
        public int SystemMaxLength;
        public int Minimum;
        public int Maximum;
        public int GreaterThanTaskParamOrder;
        public int DependentTaskParamParamOrder;
        public String ParamTableValueColumn;
        public String ParamTableTextColumn;
        public String ParamTableName;
        public boolean IsEnable;
        public boolean IsVisible;
        public String DefaultText;
        public String DefaultValue;
        public String FormatRegex;
        public int OrderNumber;
        public boolean IsRequired;
        public int ButtonCellModeId;

        @Override
        public String toString() {
            return "tp{" +
                    "TaskParamId=" + TaskParamId +
                    ", TaskId=" + TaskId +
                    ", TaskParamName='" + TaskParamName + '\'' +
                    ", IndividualTaskParamName='" + IndividualTaskParamName + '\'' +
                    ", TaskParamType='" + TaskParamType + '\'' +
                    ", TaskParamOrder=" + TaskParamOrder +
                    ", ResultTableFieldName='" + ResultTableFieldName + '\'' +
                    ", IndividualMaxLength=" + IndividualMaxLength +
                    ", SystemMaxLength=" + SystemMaxLength +
                    ", Minimum=" + Minimum +
                    ", Maximum=" + Maximum +
                    ", GreaterThanTaskParamOrder=" + GreaterThanTaskParamOrder +
                    ", DependentTaskParamParamOrder=" + DependentTaskParamParamOrder +
                    ", ParamTableValueColumn='" + ParamTableValueColumn + '\'' +
                    ", ParamTableTextColumn='" + ParamTableTextColumn + '\'' +
                    ", ParamTableName='" + ParamTableName + '\'' +
                    ", IsEnable=" + IsEnable +
                    ", IsVisible=" + IsVisible +
                    ", DefaultText='" + DefaultText + '\'' +
                    ", DefaultValue='" + DefaultValue + '\'' +
                    ", FormatRegex='" + FormatRegex + '\'' +
                    ", OrderNumber=" + OrderNumber +
                    ", IsRequired=" + IsRequired +
                    ", ButtonCellModeId=" + ButtonCellModeId +
                    '}';
        }
    }

    public boolean IsRelationRegion;
//    public String RegionR3MViews;


    @Override
    public String toString() {
        return "JsonBean{" +
                "Task=" + Task +
                ", TaskParams=" + TaskParams +
                ", IsRelationRegion=" + IsRelationRegion +
                '}';
    }
}
