<script setup>
import {ref} from "vue";
// import Multiselect from "@vueform/multiselect";
import {useLoaddingStore} from './../../store/LoaddingStore'
import {notify} from "@kyvg/vue3-notification";
import axios from "axios"
import * as XLSX from "xlsx";
import { useItemConfigStore } from "./../../store/ItemConfigStore";
// import { i18n } from '../../main.js';
// import moment from 'moment'
// var checkSameConfig = false;
// const errorMessageFile = ref("");
const pageImport = ref(1);
const totalRecord = ref();
const listDataFileShow = ref([]);
const itemConfigStore = useItemConfigStore();
const listDataFile = ref("");
const loaddingStore = useLoaddingStore()
const type = ref("");
const quantitySuccess = ref('')
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref('')
// const options = [
//   {
//     title: "Import",
//     value: "1",
//   },
// ];
const inputFile = ref(null);
const fileData = ref("");
const previewFiles = async () => {
  listDataFile.value = [];
  listDataFileShow.value = [];
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  fileData.value = inputFile.value.files[0];
  const file = fileData.value;
  if (file) {
    var reader = new FileReader();
    reader.onload = () => {
      var fileData = reader.result;
      var wb = XLSX.read(fileData, {
        type: "binary",
        cellText: false,
        cellDates: true,
      });
      listDataFile.value = XLSX.utils.sheet_to_json(
        wb.Sheets[wb.SheetNames[0]],
        {
          header: 0,
          raw: false,
          dateNF: "DD/MM/YYYY",
        }
      );
      listDataFileShow.value = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]],
        {
          header: 0,
          raw: false,
          dateNF: "DD/MM/YYYY",
        }
      );
      const dataWithStatus = listDataFileShow.value.map((item) => ({
        ...item,
        status: true,
        errorMesage: "",
      }));
      listDataFile.value = dataWithStatus;
      console.log(listDataFile.value)
      if (listDataFile.value && listDataFile.value.length) {
        totalRecord.value = listDataFile.value.length;
        myCallback();
      }
    };
    console.log(listDataFile.value)
    reader.readAsBinaryString(file);
  }
  inputFile.value.value = null;
  quantitySuccess.value = null;
  fileNameError.value = null;
};

function myCallback() {
  var i = (pageImport.value - 1) * 10;
  var j = pageImport.value * 10;
  listDataFile.value = [];
  for (i; i < j; i++) {
    if (listDataFileShow.value[i]) {
      listDataFile.value.push(listDataFileShow.value[i]);
    }
  }
}

const importBTS = async () => {
    listDataFile.value = [];
    const formData = new FormData();
    formData.append("file", fileData.value);
    if (type.value == 1) {
      formData.append("action", "ADD");
    } else if (type.value == 2) {
      formData.append("action", "DELETE");
    }
    try {
      loaddingStore.loading = true;
      const response = await axios.post("/api/sale-bts-config/import", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      console.log(response);
      if(response.data.fileImportResultName) {
      errorMessageFlag.value = true
      quantitySuccess.value = response.data.errorMessageImport
      fileNameError.value = response.data.fileImportResultName
    } else {
      listDataFile.value = [];
      successMessageFlag.value = true
    }
    
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
const downloadFileTemplate = async () => {
  itemConfigStore.downloadFile(`Template_import_BTS.xlsx`);
};

const downloadErrorImmportFile = () => {
  itemConfigStore.downloadFileError(fileNameError.value);
}
// const isImportDisabled = computed(() => {
//   return (
//      listDataFile.value.length === 0
//   );
// });
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
        <button class="btn btn-light" style="width: 100%; color: #24695c" >
          <i class="fa fa-download"></i>
          <a @click="downloadFileTemplate">
            {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE") }}
          </a>
        </button>
      </div>
    </div>
  </div>
  <div class="row">
    
  </div>
  <div v-if=" listDataFile.length" class="row mb-3 evaluations">
    <div class="table-responsive">
      <table class="table table-bordered table-hover">
        <thead
          class="table-success"
          style="background-color: #24695c; color: #fff">
          <tr>
            <th scope="col">#</th>
            <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
            <th scope="col">{{ $t("BTS_CODE") }}</th>
            <th scope="col">{{ $t("POLICY") }}</th>
            <th scope="col">{{ $t("STATUS") }}</th>
            <th scope="col">{{ $t("FROM_DATE") }}</th>
            <th scope="col">{{ $t("TO_DATE") }}</th>
          </tr>
        </thead>
        <tbody v-for="(item, index) in listDataFile" :key="item">
          <tr>
            <td>{{ index + 1 }}</td>
            <td>{{ item["Código de sucursal"] }}</td>
            <td>{{ item["BTS sitio"] }}</td>
            <td>{{ item["POLÍTICA"] }}</td>
            <td>{{ item["Estado"] }}</td>
            <td>{{ item["Fecha de inicio"] }}</td>
            <td>{{ item["Fecha final"] }}</td>
          </tr>
        </tbody>
      </table>
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
            @click="downloadErrorImmportFile(fileNameError)"
          >
            <i class="fa fa-download"></i>
            {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
          </button>
        </div>
      </div>
      <div class="row" v-if="successMessageFlag">
        <div class="form-group col-md-12">
          <div class="alert bg-success">
            <strong>{{ $t("IMPORT.ANOTATION") }}</strong>
          </div>
        </div>
      </div>

  <div class="row">
    <div class="btn-showcase text-center">
      <button
        v-if="listDataFile.length"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        style="margin-top:10px ;"
        @click="importBTS()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
      </button>
      <!-- <button
        v-if="type == 2"
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importBTS()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT_REMOVE") }}
      </button> -->
    </div>
  </div>
</template>
