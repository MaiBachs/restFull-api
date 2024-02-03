<script setup>
import { ref, watch } from "vue";
import Multiselect from "@vueform/multiselect";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import axios from "axios";
import * as XLSX from "xlsx";
import { i18n } from '../../main.js';
import moment from 'moment'

const loaddingStore = useLoaddingStore();
const type = ref("");
// var fileCheck = true;
const quantitySuccess = ref("");
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref("");
const errorMessageFile = ref("");
const options = [
  {
    title: i18n.global.t("VISIT_PLAN.IMPORT.LABEL.IMPORT"),
    value: "1",
  },
  {
    title: i18n.global.t("VISIT_PLAN.IMPORT.LABEL.IMPORT_REMOVE"),
    value: "2",
  },
];
var checkSameConfig = false;
const inputFile = ref(null);
const fileData = ref("");
const listDataFile = ref("");
const listDataFileContent = ref("");
const changeType = () => {
  listDataFile.value = [];
};
const previewFiles = () => {
  errorMessageFile.value = ""
  listDataFile.value = [];
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  fileData.value = inputFile.value.files[0];
  // let listContentFile = [{}];
  // const currentDate = moment()
  const file = fileData.value;
  if(file) {
    var reader = new FileReader();
    reader.onload = () => {
      var fileData = reader.result;
      var wb = XLSX.read(fileData, {
        type: "binary",
        cellText: false,
        cellDates: true,
      });
      listDataFileContent.value = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]], {
        header: 0,
        raw: false,
        dateNF: "DD/MM/YYYY",
      }).slice(1);

      const dataWithStatus = listDataFileContent.value.map((item) => ({
        ...item,
        status: true,
        errorMesage: "",
      }));
      listDataFile.value = dataWithStatus;
      console.log(listDataFile.value)
      if(listDataFile.value ){
        for (let index = 0; index < listDataFile.value.length;index++) {


          // const branchCode = listDataFile.value[index].__EMPTY;
          // const channelType = listDataFile.value[index].__EMPTY_1;
          // const jobCode = listDataFile.value[index].__EMPTY_2;
          // const quantity_per_month = listDataFile.value[index].__EMPTY_3;
          // const approval_score = listDataFile.value[index].__EMPTY_4;
          // const dateEvaluation1 = moment(listDataFile.value[index].__EMPTY_5,"DD/MM/yyyy",true);
          // // console.log(listDataFile.value[index].__EMPTY_5)
          // let ddMMyyyy1 =  listDataFile.value[index].__EMPTY_5 ? moment(listDataFile.value[index].__EMPTY_5,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
          // let ddMMyyyy2 =  listDataFile.value[index].__EMPTY_6 ? moment(listDataFile.value[index].__EMPTY_6,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
          // let ddMMyyyy3 =  listDataFile.value[index].__EMPTY_7 ? moment(listDataFile.value[index].__EMPTY_7,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
          // let ddMMyyyy4 =  listDataFile.value[index].__EMPTY_8 ? moment(listDataFile.value[index].__EMPTY_8,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
          // const monthYear1 =  listDataFile.value[index].__EMPTY_5 ? moment(listDataFile.value[index].__EMPTY_5,"DD/MM/yyyy").format("MM-YYYY") : null;
          // const monthYear2 =  listDataFile.value[index].__EMPTY_6 ? moment(listDataFile.value[index].__EMPTY_6,"DD/MM/yyyy").format("MM-YYYY") : null;
          // const monthYear3 =  listDataFile.value[index].__EMPTY_7 ? moment(listDataFile.value[index].__EMPTY_7,"DD/MM/yyyy").format("MM-YYYY") : null;
          // const monthYear4 =  listDataFile.value[index].__EMPTY_8 ? moment(listDataFile.value[index].__EMPTY_8,"DD/MM/yyyy").format("MM-YYYY") : null;
         
          for (let j = index + 1; j < listDataFile.value.length; j++) {
            // const dateEvaluation1j = moment(listDataFile.value[j].__EMPTY_5,"DD/MM/yyyy",true);
            // let ddMMyyyy1j =  listDataFile.value[j].__EMPTY_5 ? moment(listDataFile.value[j].__EMPTY_5,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
            // let ddMMyyyy2j =  listDataFile.value[j].__EMPTY_6 ? moment(listDataFile.value[j].__EMPTY_6,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
            // let ddMMyyyy3j =  listDataFile.value[j].__EMPTY_7 ? moment(listDataFile.value[j].__EMPTY_7,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
            // let ddMMyyyy4j =  listDataFile.value[j].__EMPTY_8 ? moment(listDataFile.value[j].__EMPTY_8,"DD/MM/yyyy",true).format("DD-MM-YYYY") : null;
            // const monthYear1j =  listDataFile.value[j].__EMPTY_5 ? moment(listDataFile.value[j].__EMPTY_5,"DD/MM/yyyy").format("MM-YYYY") : null;
            // const monthYear2j =  listDataFile.value[j].__EMPTY_6 ? moment(listDataFile.value[j].__EMPTY_6,"DD/MM/yyyy").format("MM-YYYY") : null;
            // const monthYear3j =  listDataFile.value[j].__EMPTY_7 ? moment(listDataFile.value[j].__EMPTY_7,"DD/MM/yyyy").format("MM-YYYY") : null;
            // const monthYear4j =  listDataFile.value[j].__EMPTY_8 ? moment(listDataFile.value[j].__EMPTY_8,"DD/MM/yyyy").format("MM-YYYY") : null;
            // So sánh giá trị của hai phần tử
            if (listDataFile.value[index].__EMPTY == undefined ? "" : listDataFile.value[index].__EMPTY == listDataFile.value[j].__EMPTY
              && listDataFile.value[index].__EMPTY_1 == listDataFile.value[j].__EMPTY_1
              && listDataFile.value[index].__EMPTY_2 == listDataFile.value[j].__EMPTY_2
              && moment(listDataFile.value[index].__EMPTY_5, "DD/MM/yyyy").format("MM-YYYY") == moment(listDataFile.value[j].__EMPTY_5, "DD/MM/yyyy").format("MM-YYYY")) {
              checkSameConfig = true;
              listDataFile.value[index].status = false;
              listDataFile.value[j].status = false;

              console.log(checkSameConfig)
              if (checkSameConfig) {
                errorMessageFile.value = i18n.global.t('OVERWRITTEN.ROW');
                listDataFile.value[index].errorMesage = i18n.global.t('OVERWRITTEN.ROW');
                listDataFile.value[j].errorMesage = i18n.global.t('OVERWRITTEN.ROW');
                // fileCheck = false
                console.log("log16")
              }
            }
            // else{
            //   if (dateEvaluation1.isBefore(currentDate)) {
            //   errorMessageFile.value = i18n.global.t('TIME.GREATER.CURRENT.DATE')
            //   // fileCheck = false;
            //   listDataFile.value[index].status = false;
            //   listDataFile.value[index].errorMesage = i18n.global.t('TIME.GREATER.CURRENT.DATE')
            //   console.log("trước ngày hiện tại")
            //   // return;
            // }if(dateEvaluation1j.isBefore(currentDate)){
            //   errorMessageFile.value = i18n.global.t('TIME.GREATER.CURRENT.DATE')
            //   // fileCheck = false;
            //   listDataFile.value[j].status = false;
            //   listDataFile.value[j].errorMesage = i18n.global.t('TIME.GREATER.CURRENT.DATE')
            //   console.log("trước ngày hiện tại")
            //   // return;
            // }
            // }
            // else {
              // checkSameConfig = false
            //   if ((branchCode === undefined || channelType === undefined || jobCode === undefined) && (listDataFile.value[j].__EMPTY !== undefined || listDataFile.value[j].__EMPTY_1 !== undefined || listDataFile.value[j].__EMPTY_2 !== undefined)) {
            //     // console.log("rỗng 2")
            //     errorMessageFile.value = i18n.global.t('EMPTY.ROW');
            //     // fileCheck = false;
            //     listDataFile.value[index].status = false;
            //     listDataFile.value[index].errorMesage = i18n.global.t('EMPTY.ROW');
            //   } if ((branchCode !== undefined || channelType !== undefined || jobCode !== undefined) && (listDataFile.value[j].__EMPTY === undefined || listDataFile.value[j].__EMPTY_1 === undefined || listDataFile.value[j].__EMPTY_2 === undefined)) {
            //     // console.log("rỗng 3")
            //     errorMessageFile.value = i18n.global.t('EMPTY.ROW');
            //     // fileCheck = false;
            //     listDataFile.value[j].status = false;
            //     listDataFile.value[j].errorMesage = i18n.global.t('EMPTY.ROW');
            //   }
            //   //  if (branchCode !== undefined && channelType !== undefined && jobCode !== undefined && listDataFile.value[j].__EMPTY !== undefined && listDataFile.value[j].__EMPTY_1 !== undefined && listDataFile.value[j].__EMPTY_2 !== undefined) {
            //   //   checkSameConfig = false;
            //   //   console.log('lád')
            //   // }
            //   if ((branchCode === undefined || channelType === undefined || jobCode === undefined) && (listDataFile.value[j].__EMPTY === undefined || listDataFile.value[j].__EMPTY_1 === undefined || listDataFile.value[j].__EMPTY_2 === undefined)) {
            //     console.log("rỗng 1")
            //     errorMessageFile.value = i18n.global.t('EMPTY.ROW');
            //     // fileCheck = false;
            //     listDataFile.value[index].status = false;
            //     listDataFile.value[j].status = false;
            //     listDataFile.value[index].errorMesage = i18n.global.t('EMPTY.ROW');
            //     listDataFile.value[j].errorMesage = i18n.global.t('EMPTY.ROW');
            //   }
            // }

            // if (approval_score <= 0 || approval_score > 100 || approval_score === undefined) {
            //   errorMessageFile.value = i18n.global.t('APPROVAL.SCORE');
            //   // fileCheck = false;
            //   listDataFile.value[index].status = false;
            //   listDataFile.value[index].errorMesage = i18n.global.t('APPROVAL.SCORE');
            //   // return;
            // }else if(listDataFile.value[j].__EMPTY_4 <= 0 || listDataFile.value[j].__EMPTY_4 > 100 || listDataFile.value[j].__EMPTY_4 === undefined){
            //   errorMessageFile.value = i18n.global.t('APPROVAL.SCORE');
            //   // fileCheck = false;
            //   listDataFile.value[j].status = false;
            //   listDataFile.value[j].errorMesage = i18n.global.t('APPROVAL.SCORE');
            //   // return;
            // }else{
            //   // fileCheck = true;
            // }
            // if (quantity_per_month === undefined) {
            //   errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.NULL');
            //   // fileCheck = false;
            //   listDataFile.value[index].status = false;
            //   listDataFile.value[index].errorMesage =i18n.global.t('QUANTITY.EVALUATION.NULL');
            //   console.log("tháng")
            //   // return;
            // }else if(listDataFile.value[j].__EMPTY_3 === undefined){
            //   errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.NULL');
            //   // fileCheck = false;
            //   listDataFile.value[j].status = false;
            //   listDataFile.value[j].errorMesage =i18n.global.t('QUANTITY.EVALUATION.NULL');
            //   console.log("tháng")
            // }
            // else if (quantity_per_month <= 0 || quantity_per_month > 4) {
            //   errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.CHECK');
            //   // fileCheck = false;
            //   listDataFile.value[index].status = false;
            //   listDataFile.value[index].errorMesage = i18n.global.t('QUANTITY.EVALUATION.CHECK');
            //   console.log("tháng lớn hơn 4")
            // }
            // else if (listDataFile.value[j].__EMPTY_3 <= 0 || listDataFile.value[j].__EMPTY_3 > 4) {
            //   errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.CHECK');
            //   // fileCheck = false;
            //   listDataFile.value[j].status = false;
            //   listDataFile.value[j].errorMesage = i18n.global.t('QUANTITY.EVALUATION.CHECK');
            //   console.log("tháng lớn hơn 4")
            // }
            

//             if (quantity_per_month == 1 && ddMMyyyy1 != null) {
//               ddMMyyyy2 = null;
//               ddMMyyyy3 = null;
//               ddMMyyyy4 = null;
//             } else if (quantity_per_month == 2 && ddMMyyyy1 != null && ddMMyyyy2 != null) {
//               // console.log("tdsa")
//               if (ddMMyyyy3 == null && ddMMyyyy4 == null) {
//                 if (ddMMyyyy1 >= ddMMyyyy2) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MUST_BE_DATE')
//                   // fileCheck = false;
//                   listDataFile.value[index].status = false;
//                   listDataFile.value[index].errorMesage = i18n.global.t('TIME.GREATER.CURRENT.DATE')
//                   console.log("log5")
//                 } if (monthYear1 != monthYear2) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH')
//                   // fileCheck = false;
//                   listDataFile.value[index].status = false;
//                   console.log("log6")
//                 }
//               } else {
//                 errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.DATE')
//                 // fileCheck = false;
//                 listDataFile.value[index].status = false;
//                 console.log("log7")
//               }
//               ddMMyyyy3 = null;
//               ddMMyyyy4 = null;

//             } else if (quantity_per_month == 3 && ddMMyyyy1 != null && ddMMyyyy2 != null && ddMMyyyy3 != null) {
//               // console.log(ddMMyyyy4)
//               if (ddMMyyyy4 == null) {
//                 // console.log("4 = null")
//                 if (ddMMyyyy1 >= ddMMyyyy2 || ddMMyyyy1 >= ddMMyyyy3 || ddMMyyyy2 >= ddMMyyyy3) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MUST_BE_DATE') + 2;
//                   // fileCheck = false;
//                   listDataFile.value[index].status = false;
//                   console.log("log8")
//                 } if (monthYear1 != monthYear2 || monthYear1 != monthYear3 || monthYear2 != monthYear3) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH') + 3;
//                   // fileCheck = false;
//                   listDataFile.value[index].status = false;
//                   console.log("log9")
//                 }
//               } else {
//                 // console.log("4 != null")
//                 errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.DATE') + 2;
//                 // fileCheck = false;
//                 listDataFile.value[index].status = false;
//                 console.log("log10")
//               }
//               ddMMyyyy4 = null;

//             } else if (quantity_per_month == 4 && ddMMyyyy1 != null && ddMMyyyy2 != null && ddMMyyyy3 != null && ddMMyyyy4 != null) {
//               if (ddMMyyyy1 >= ddMMyyyy2 || ddMMyyyy1 >= ddMMyyyy3 || ddMMyyyy2 >= ddMMyyyy3 || ddMMyyyy3 >= ddMMyyyy4
//                 || ddMMyyyy1 >= ddMMyyyy4 || ddMMyyyy2 >= ddMMyyyy4
//               ) {
//                 errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MUST_BE_DATE') + 3;
//                 // fileCheck = false;
//                 listDataFile.value[index].status = false;
//                 console.log("log11")
//               } if (monthYear1 != monthYear2 || monthYear1 != monthYear3 || monthYear1 != monthYear4 &&
//                 monthYear2 != monthYear3 || monthYear2 != monthYear4 ||
//                 monthYear3 != monthYear4) {
//                 errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH') + 4;
//                 // fileCheck = false;
//                 listDataFile.value[index].status = false;
//                 console.log("log12")
//               }
//               if (monthYear1 !== monthYear2 ||
//                 monthYear1 !== monthYear3 ||
//                 monthYear1 !== monthYear4 ||
//                 monthYear2 !== monthYear3 ||
//                 monthYear2 !== monthYear4 ||
//                 monthYear3 !== monthYear4) {
//                 errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH') + 1;
//                 // fileCheck = false;
//                 listDataFile.value[index].status = false;
//                 console.log("log13")
//               }
//             } else {
//               errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.DATE') + 3;
//               // fileCheck = false;
//               listDataFile.value[index].status = false;
//               console.log("log14")
//             }
// //  -----------------------------------------------------------------------
//             if (listDataFile.value[j].__EMPTY_3 == 1 && ddMMyyyy1j != null) {
//               ddMMyyyy2j = null;
//               ddMMyyyy3j = null;
//               ddMMyyyy4j = null;
//             } else if (listDataFile.value[j].__EMPTY_3 == 2 && ddMMyyyy1j != null && ddMMyyyy2j != null) {
//               // console.log("tdsa")
//               if (ddMMyyyy3j == null && ddMMyyyy4j == null) {
//                 if (ddMMyyyy1j >= ddMMyyyy2j) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MUST_BE_DATE') + 1;
//                   // fileCheck = false;
//                   listDataFile.value[j].status = false;
//                   console.log("log5")
//                 } if (monthYear1j != monthYear2j) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH') + 2;
//                   // fileCheck = false;
//                   listDataFile.value[j].status = false;
//                   console.log("log6")
//                 }
//               } else {
//                 errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.DATE') + 1;
//                 // fileCheck = false;
//                 listDataFile.value[j].status = false;
//                 console.log("log7")
//               }
//               ddMMyyyy3j = null;
//               ddMMyyyy4j = null;

//             } else if (listDataFile.value[j].__EMPTY_3 == 3 && ddMMyyyy1j != null && ddMMyyyy2j != null && ddMMyyyy3j != null) {
//               // console.log(ddMMyyyy4j)
//               if (ddMMyyyy4j == null) {
//                 // console.log("4 = null")
//                 if (ddMMyyyy1j >= ddMMyyyy2j || ddMMyyyy1j >= ddMMyyyy3j || ddMMyyyy2j >= ddMMyyyy3j) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MUST_BE_DATE') + 2;
//                   // fileCheck = false;
//                   listDataFile.value[j].status = false;
//                   console.log("log8")
//                 } if (monthYear1j != monthYear2j || monthYear1j != monthYear3j || monthYear2j != monthYear3j) {
//                   errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH') + 3;
//                   // fileCheck = false;
//                   listDataFile.value[j].status = false;
//                   console.log("log9")
//                 }
//               } else {
//                 // console.log("4 != null")
//                 errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.DATE') + 2;
//                 // fileCheck = false;
//                 listDataFile.value[j].status = false;
//                 console.log("log10")
//               }
//               ddMMyyyy4j = null;

//             } else if (listDataFile.value[j].__EMPTY_3 == 4 && ddMMyyyy1j != null && ddMMyyyy2j != null && ddMMyyyy3j != null && ddMMyyyy4j != null) {
//               if (ddMMyyyy1j >= ddMMyyyy2j || ddMMyyyy1j >= ddMMyyyy3j || ddMMyyyy2j >= ddMMyyyy3j || ddMMyyyy3j >= ddMMyyyy4j
//                 || ddMMyyyy1j >= ddMMyyyy4j || ddMMyyyy2j >= ddMMyyyy4j
//               ) {
//                 errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MUST_BE_DATE') + 3;
//                 // fileCheck = false;
//                 listDataFile.value[j].status = false;
//                 console.log("log11")
//               } if (monthYear1j != monthYear2j || monthYear1j != monthYear3j || monthYear1j != monthYear4j &&
//                 monthYear2j != monthYear3j || monthYear2j != monthYear4j ||
//                 monthYear3j != monthYear4j) {
//                 errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH') + 4;
//                 // fileCheck = false;
//                 listDataFile.value[j].status = false;
//                 console.log("log12")
//               }
//               if (monthYear1j !== monthYear2j ||
//                 monthYear1j !== monthYear3j ||
//                 monthYear1j !== monthYear4j ||
//                 monthYear2j !== monthYear3j ||
//                 monthYear2j !== monthYear4j ||
//                 monthYear3j !== monthYear4j) {
//                 errorMessageFile.value = i18n.global.t('EVALUATION_TIME_MONTH') + 1;
//                 // fileCheck = false;
//                 listDataFile.value[j].status = false;
//                 console.log("log13")
//               }
//             } else {
//               errorMessageFile.value = i18n.global.t('QUANTITY.EVALUATION.DATE') + 3;
//               // fileCheck = false;
//               listDataFile.value[j].status = false;
//               console.log("log14")
//             }

          }

          // if(fileCheck){
          //   index++
          // }else{
          //   console.log('ket thuc')
          //   return
          // }
        }
      }
    };

    // console.log(errorMessageFile.value)
    // console.log(fileCheck)
    reader.readAsBinaryString(file);
  }
  
  inputFile.value.value = null
};

const importEvaluations = async () => {
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  const formData = new FormData();
  formData.append("file", fileData.value);
  if (type.value == 1) {
    formData.append("action", "ADD");
  } else if (type.value == 2) {
    formData.append("action", "DELETE");
  }
  try {
    loaddingStore.loading = true;
    const response = await axios.post(
      "/api/evaluation-config/import",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    if (response.data.data == null) {
      quantitySuccess.value = response.data.status.message;
      fileNameError.value = response.data.status.code;
    }else{
      quantitySuccess.value = response.data.status.message;
    }
    if (fileNameError.value) {
      errorMessageFlag.value = true;
    } else {
      successMessageFlag.value = true;
    }
    listDataFile.value = [];
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    successMessageFlag.value = false; 
    listDataFile.value = [];
    errorMessageFlag.value = false;
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 500,
    });
  }
};

const downloadErrorImmportFile = async (fileName) => {
  await axios.get(`api/download-template/${fileName}/1`, { responseType: 'blob' }).then((response) => {
        const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        response.headers['content-disposition',`attachment; filename="${fileName}.xls"`];
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        link.remove();
      });
};

const downloadFileTemplate = async () => {
  try {
    loaddingStore.loading = true;
    await axios.get(`api/download-template/template_import_evaluation.xls/0`, { responseType: 'blob' }).then((response) => {
      const blob = new Blob([response.data], { type: 'application/vnd.ms-excel' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      response.headers['content-disposition', `attachment; filename="template_import_evaluation.xls"`];
      link.setAttribute('download', "template_import_evaluation");
      document.body.appendChild(link);
      link.click();
      link.remove();
    });
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    console.log(error);
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 500,
    });
  }
};
watch(type, (newValue) => {
  if (!newValue) {
    successMessageFlag.value = false;
    listDataFile.value = [];
    errorMessageFlag.value = false;
  }
});

</script>
<template>
  <div class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label"
          >{{ $t("TYPE_OF_ACTION") }}:</label
        >
        <div class="col-sm-6">
          <multiselect
            v-model="type"
            v-on:change="changeType()"
            deselect-label="Can't remove this value"
            track-by="title"
            label="title"
            :placeholder="$t('SELECT_ONE')"
            :options="options"
            :searchable="false"
            :allow-empty="false"
          >
          </multiselect>
        </div>
      </div>
    </div>
    <div class="col-md-4 text-center">
      <div class="mb-3 row">
        <button class="btn btn-light" style="width: 100%; color: #24695c">
          <i class="fa fa-download"></i>
          <a @click="downloadFileTemplate">
            {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE") }}
          </a>
        </button>
      </div>
    </div>
  </div>
  <div v-if="type" class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label text-left"
          >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
        >
        <div class="col-sm-8 col-form-label">
          <div class="">
            <form>
              <input
                @change="previewFiles"
                id="file"
                ref="inputFile"
                accept=".xls, .xlsx"
                type="file"
              />
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div v-if="type && listDataFile.length" class="row mb-3 evaluations">
    <div class="table-responsive">
      <table class="table table-bordered table-hover">
        <thead
          class="table-success"
          style="background-color: #24695c; color: #fff"
        >
          <tr>
            <th scope="col">#</th>
            <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
            <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
            <th scope="col">{{ $t("EVALUATION") }}</th>
            <th scope="col">{{ $t("QUANTITY_PER_MONTH") }}</th>
            <th scope="col">{{ $t("APPROVAL_SCORE") }}</th>
            <th scope="col">{{ $t("DATE_OF_EVALUTION") }} 1</th>
            <th scope="col">{{ $t("DATE_OF_EVALUTION") }} 2</th>
            <th scope="col">{{ $t("DATE_OF_EVALUTION") }} 3</th>
            <th scope="col">{{ $t("DATE_OF_EVALUTION") }} 4</th>
            <!-- <th scope="col">{{ $t("STATUS") }}</th> -->
          </tr>
        </thead>
        <tbody v-for="(item, index) in listDataFile" :key="item">
          <tr>
            <td>{{ index + 1 }}</td>
            <td>{{ item.__EMPTY }}</td>
            <td>{{ item.__EMPTY_1 }}</td>
            <td>{{ item.__EMPTY_2 }}</td>
            <td>{{ item.__EMPTY_3 }}</td>
            <td>{{ item.__EMPTY_4 }}</td>
            <td>{{ item.__EMPTY_5 }}</td>
            <td>{{ item.__EMPTY_6 }}</td>
            <td>{{ item.__EMPTY_7 }}</td>
            <td>{{ item.__EMPTY_8 }}</td>
            <!-- <td :style="{backgroundColor: item.status===true ? '#3ead3e' : 'red'}" style="color: #fff;">{{ item.status?'OK':'NOK' }}</td> -->
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div class="row mb-3 p-b-15" v-if="errorMessageFlag">
    <div class="form-group col-md-6">
      <div class="alert bg-danger">
        <strong>{{ quantitySuccess }}</strong>
      </div>
    </div>
    <div class="col-xs-6 col-md-6 col-sm-6" style="margin-top: 10px">
      <button
        class="btn bg-danger"
        type="button"
        @click="downloadErrorImmportFile(fileNameError)"
      >
        <i class="fa fa-download"></i>
        {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
      </button>
    </div>
  </div>
  <div class="row p-b-15" v-if="successMessageFlag">
    <div class="form-group col-md-12">
      <div class="alert bg-success">
        <strong>{{ quantitySuccess }}</strong>
      </div>
    </div>
  </div>

  <div class="row mt-3 text-center">
    <p style="color: red; font-weight: bold;" v-if="errorMessageFile.length">{{ errorMessageFile }}</p>
    <div class="btn-showcase text-center" v-if="errorMessageFile.length==0">
      <button
        v-if="type == 1"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importEvaluations()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
      </button>
      <button
        v-if="type == 2"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importEvaluations()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT_REMOVE") }}
      </button>
    </div>
  </div>
</template>
