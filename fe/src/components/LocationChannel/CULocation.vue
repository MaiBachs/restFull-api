<script setup>
import { ref } from "vue";
// import Multiselect from "@vueform/multiselect";
import { useLoaddingStore } from "../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import { useItemConfigStore } from "./../../store/ItemConfigStore";
import axios from "axios";
import * as XLSX from "xlsx";
import { useEvalutionStore } from "@/store/EvalutionStore";
// import { i18n } from '../../main.js';
const loaddingStore = useLoaddingStore();
const evaluationConfig = useEvalutionStore();
const itemConfigStore = useItemConfigStore();
const quantitySuccess = ref("");
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref("");
const listDataFile = ref("");
const inputFile = ref(null);
const fileData = ref("");
const totalRecord = ref()
const pageImport = ref(1)
const listDataFileShow = ref([])

const previewFiles = async () => {
  listDataFile.value = [];
  listDataFileShow.value = [];
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  fileData.value = inputFile.value.files[0];
  console.log(fileData.value)
  const file = fileData.value; // Lấy tệp đầu tiên nếu có nhiều tệp
  if(file) {
    var reader = new FileReader();
    reader.onload = () => {
      var fileData = reader.result;
      var wb = XLSX.read(fileData, {
        type: "binary",
        cellText: false,
        cellDates: true,
      });
      listDataFile.value = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]], {
        header: 0,
        raw: false,
        dateNF: "DD/MM/YYYY",
      });
      listDataFileShow.value = listDataFile.value = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]], {
        header: 0,
        raw: false,
        dateNF: "DD/MM/YYYY",
      });
    
    if(listDataFile.value && listDataFile.value.length) {
      totalRecord.value = listDataFile.value.length
      myCallback()
    }
    };
    reader.readAsBinaryString(file);
  }
  inputFile.value.value = null
  quantitySuccess.value = null;
  fileNameError.value = null;
};

function myCallback() {
    var i = (pageImport.value - 1) * 10;
    var j = pageImport.value * 10;
    listDataFile.value = [];
    for(i; i<j; i++) {
        if(listDataFileShow.value[i]) {
            listDataFile.value.push(listDataFileShow.value[i]);
            
        }
    }
}

const importUpdateLocation = async () => {
  listDataFile.value = [];
  listDataFileShow.value = [];
  const formData = new FormData();
  formData.append("file", fileData.value);

  try {
    loaddingStore.loading = true;
    const response = await axios.post("/api/Location-manager/import", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    quantitySuccess.value = response.data.status.message;
    fileNameError.value = response.data.status.code;
    if (response.data.data) {
      successMessageFlag.value = true;
    } else {
      errorMessageFlag.value = true;
    }
    loaddingStore.loading = false;
  } catch (error) {
    loaddingStore.loading = false;
    notify({
      type: "error",
      title: "Get current user",
      text: error,
      duration: 1555,
    });
  }
};

const downloadErrorImmportFile = () => {
  itemConfigStore.downloadFileError(fileNameError.value,1);
};

const downloadFileTemplate = async () => {
  evaluationConfig.downloadFileTemplate("template_import_lastLong_channel_Active.xlsx",0);
};




</script>
<template>
  <div class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label text-left"
          >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
        >
        <div class="col-sm-8 col-form-label">
          <input
            id="file"
            ref="inputFile"
            accept=".xls, .xlsx"
            type="file"
            @change="previewFiles"
          />
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
  <div class="rowmb-3">
    <div class="col-md-6 mb-4">
    </div>
  </div>

  <div v-if="listDataFile.length" class="row mb-3 evaluations">
    <div class="table-responsive">
      <table class="table table-bordered table-hover">
        <thead
          class="table-success"
          style="background-color: #24695c; color: #fff"
        >
          <tr>
            <th scope="col">#</th>
            <th scope="col">{{ $t("CHANNEL_CODE") }}</th>
            <th scope="col">{{ $t("LOCATION.LATIT") }}</th>
            <th scope="col">{{ $t("LOCATION.LONGIT") }}</th>
            <th scope="col">{{ $t("LOCATION.RADIUS") }}</th>
          </tr>
        </thead>
        <tbody v-for="(item, index) in listDataFile.slice(1)" :key="item">
          <tr>
            <td>{{ (pageImport - 1)* 10 +(index+1)}}</td>
            <td>{{ item.__EMPTY }}</td>
            <td>{{ item.__EMPTY_1 }}</td>
            <td>{{ item.__EMPTY_2 }}</td>
            <td>{{ item.__EMPTY_3 }}</td>
          </tr>
        </tbody>
      </table>
      <div class="pagination-search">
        <pagination v-model="pageImport" :records="totalRecord" :per-page="10" @paginate="myCallback"/>
      </div>
    </div>
  </div>

  <div class="row mb-3" v-if="errorMessageFlag">
    <div class="form-group col-md-6">
      <div class="alert bg-danger">
        <strong>{{ quantitySuccess }}</strong>
      </div>
    </div>
    <div class="col-xs-6 col-md-6 col-sm-6" style="margin-top: 10px">
      <button
        class="btn bg-danger"
        type="button"
        @click="downloadErrorImmportFile"
      >
        <i class="fa fa-download"></i>
        {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
      </button>
    </div>
  </div>
  <div class="row" v-if="successMessageFlag === true">
    <div class="form-group col-md-12">
      <div class="alert bg-success">
        <strong>{{ $t("IMPORT_SUCCESS") }}</strong>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="btn-showcase text-center">
      <button
        v-if="listDataFile.length"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importUpdateLocation()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
      </button>
    </div>
  </div>
</template>
