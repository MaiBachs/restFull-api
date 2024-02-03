<script setup>
import { ref, watch } from "vue";
import * as XLSX from "xlsx";
import { notify } from "@kyvg/vue3-notification";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import axios from "axios";
const errorMessageFile = ref("");
const type = ref("");
const loaddingStore = useLoaddingStore();
const listDataFile = ref("");
const listDataFileShow = ref([]);
const fileNameError = ref("");
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const totalRecord = ref();
const quantitySuccess = ref("");
const pageImport = ref(1);
const inputFile = ref(null);
const fileData = ref("");
const previewFiles = async () => {
  errorMessageFile.value = ""
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
      listDataFileShow.value = XLSX.utils.sheet_to_json(
        wb.Sheets[wb.SheetNames[0]],
        {
          header: 0,
          raw: false,
          dateNF: "DD/MM/YYYY",
        }
      );
      // listDataFileShow.value.shift();
      const dataWithStatus = listDataFileShow.value.map((item) => ({
        ...item,
        status: true,
        errorMesage: "",
      }));
      listDataFile.value = dataWithStatus;
      // listDataFile.value.shift();
      console.log(listDataFile.value);
      if (listDataFile.value && listDataFile.value.length) {
        totalRecord.value = listDataFile.value.length;
        myCallback();
      }
    };
    console.log(listDataFile.value);
    reader.readAsBinaryString(file);
  }
  inputFile.value.value = null;
  quantitySuccess.value = null;
  fileNameError.value = null;
};
const downloadFileTemplate = async () => {
  try {
    loaddingStore.loading = true;
    await axios
      .get(`api/download-template/Template_import_survey_config.xlsx/0`, {
        responseType: "blob",
      })
      .then((response) => {
        const blob = new Blob([response.data], {
          type: "application/vnd.ms-excel",
        });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        response.headers[
          ("content-disposition",
          `attachment; filename="Template_import_survey_config.xlsx"`)
        ];
        link.setAttribute("download", "Template_import_survey_config");
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
const downloadErrorImmportFile = async (fileName) => {
  await axios
    .get(`api/download-template/${fileName}/1`, { responseType: "blob" })
    .then((response) => {
      const blob = new Blob([response.data], {
        type: "application/vnd.ms-excel",
      });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      response.headers[
        ("content-disposition", `attachment; filename="${fileName}.xls"`)
      ];
      link.setAttribute("download", fileName);
      document.body.appendChild(link);
      link.click();
      link.remove();
    });
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
const importPenalty = async () => {
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
      const response = await axios.post("/api/survey/pvd-import-surveyac", formData, {
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
        <a
          class="btn btn-light"
          style="width: 100%; color: #24695c"
          @click="downloadFileTemplate"
        >
          <i class="fa fa-download"></i>
          {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE") }}
        </a>
      </div>
    </div>
  </div>
  <div v-if="type" class="row">
    <div class="col-md-6">
      <div class="mb-3 row">
        <label class="col-sm-3 col-form-label text-left"
          >{{ $t("DATA_FILE") }}:</label
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
  </div>
  <div v-if="listDataFile.length" class="row mb-3 evaluations">
    <div class="table-responsive">
      <table class="table table-bordered table-hover">
        <thead
          class="table-success"
          style="background-color: #24695c; color: #fff"
        >
          <tr>
            <th scope="col">No</th>
            <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}</th>
            <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.BC") }}</th>
            <th scope="col">{{ $t("USER") }}</th>
            <th scope="col">{{ $t("HOME.BTN.CHANNEL") }}</th>
            <th scope="col">{{ $t("RESEARCH_DATE") }}</th>
          </tr>
        </thead>
        <tbody v-for="(item, index) in listDataFile" :key="item">
          <tr>
            <td>{{ index + 1 }}</td>
            <td>{{ item["BR"] }}</td>
            <td>{{ item["BC"] }}</td>
            <td>{{ item["CÓDIGO DE PERSONAL"] }}</td>
            <td>{{ item["CÓDIGO DE CANAL"] }}</td>
            <td>{{ item["FECHA DE INVESTIGACION"] }}</td>
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
    <div class="btn-showcase text-center" v-if="listDataFile.length">
      <button
        class="btn btn-pill btn-primary-gradien"
        type="button"
        @click="importPenalty()"
      >
        {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
      </button>
    </div>
  </div>
</template>
