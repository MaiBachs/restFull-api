<script setup>
import { ref, watch } from "vue";
import Multiselect from "@vueform/multiselect";
import { useItemConfigStore } from "./../../store/ItemConfigStore";
import * as XLSX from 'xlsx'
import { notify } from "@kyvg/vue3-notification";
import axios from "axios";
import { useLoaddingStore } from './../../store/LoaddingStore'

const loaddingStore = useLoaddingStore()
const type = ref("");
const itemConfigStore = useItemConfigStore();
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref('');
const listDataFile = ref("");
const quantitySuccess = ref('')


const changeType = () => {
    listDataFile.value = []
}

const options = [
    {
        title: "Import",
        value: "1",
    },
    {
        title: "Remove",
        value: "2",
    },
];

const inputFile = ref(null);
const fileData = ref("");
const previewFiles = async () => {
    fileNameError.value = "";
    successMessageFlag.value = false;
    errorMessageFlag.value = false
    fileData.value = inputFile.value.files[0];

    const file = fileData.value; // Lấy tệp đầu tiên nếu có nhiều tệp
    var reader = new FileReader();
    console.log(reader)
    reader.onload = () => {
        var fileData = reader.result;
        var wb = XLSX.read(fileData, {
            type: "binary",
            cellText: false,
            cellDates: true,
        });
        listDataFile.value = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]], { header: 0, raw: true, dateNF: 'DD/MM/YYYY' });
    };
    console.log(listDataFile)
    reader.readAsBinaryString(file);

};

const importItemEvaluations = async () => {
    try {
        const formData = new FormData();
        formData.append("file", fileData.value);
        if (type.value == 1) {
            formData.append("action", "ADD");
        } else if (type.value == 2) {
            formData.append("action", "DELETE");
        }

        loaddingStore.loading = true;
        const response = await axios.post("/api/item-config/import", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        });
        if (response.data.data != null) {
            quantitySuccess.value = response.data.data;
            if (response.data.status.message) {
                fileNameError.value = response.data.status.message;
            }
        } else {
            quantitySuccess.value = "success";
            fileNameError.value = null;
        }
        if (fileNameError.value) {
            successMessageFlag.value = false
            errorMessageFlag.value = true
        } else {
            errorMessageFlag.value = false
            successMessageFlag.value = true
        }
        listDataFile.value = [];
        loaddingStore.loading = false;
    } catch (error) {
        loaddingStore.loading = false;
        successMessageFlag.value = false; 
        listDataFile.value = []; 
        errorMessageFlag.value = false;
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
    itemConfigStore.downloadFileTemplate(`Template_import_item_rendata.xls`)
}

const downloadErrorImmportFile = () => {
    itemConfigStore.downloadFileError(fileNameError.value)
}
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
                <label class="col-sm-3 col-form-label">{{ $t("HOME.SEARCH.MAP.ACTION") }}</label>
                <div class="col-sm-6">
                    <multiselect 
                    v-model="type" 
                    deselect-label="Can't remove this value" 
                    track-by="title" 
                    label="title"
                    :placeholder="$t('SELECT_ONE')" 
                    :options="options" v-on:change="changeType()" 
                    :searchable="false"
                    :allow-empty="false">
                    </multiselect>
                </div>
            </div>
        </div>
        <div class="col-md-4 text-center">
            <div class="mb-3 row">
                <button class="btn btn-light" style="width: 100%; color: #24695c" @click="downloadFileTemplate">
                    <i class="fa fa-download"></i>
                    {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE") }}
                </button>
            </div>
        </div>
    </div>
    <div v-if="type" class="row">
        <div class="col-md-6">
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label text-left">{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label>
                <div class="col-sm-8 col-form-label">
                    <input id="file" ref="inputFile" accept=".xls, .xlsx" type="file" @change="previewFiles" />
                </div>
            </div>

            

            <div class="row mb-3" v-if="errorMessageFlag && type == 1" >
                <div class="form-group col-md-6">
                    <div class="alert bg-danger">
                        <strong>{{ $t("IMPORT.ANOTATION") }} {{ quantitySuccess }}</strong>
                    </div>
                </div>
                <div class="col-xs-6 col-md-6 col-sm-6" style="margin-top: 10px;">
                    <button class="btn bg-danger" type="button" @click="downloadErrorImmportFile(fileNameError)">
                        <i class="fa fa-download"></i>
                        {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
                    </button>
                </div>
            </div>
            <div class="row mb-3" v-if="errorMessageFlag && type == 2" >
                <div class="form-group col-md-6">
                    <div class="alert bg-danger">
                        <strong>{{ $t("REMOVE.ANOTATION") }} {{ quantitySuccess }}</strong>
                    </div>
                </div>
                <div class="col-xs-6 col-md-6 col-sm-6" style="margin-top: 10px;">
                    <button class="btn bg-danger" type="button" @click="downloadErrorImmportFile(fileNameError)">
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
        </div>
        <div v-if="type && listDataFile.length" class="row mb-12 evaluations">
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-success" style="background-color: #24695c; color: #fff;">
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
                            <th scope="col">{{ $t("GROUP_ITEMS") }}</th>
                            <th scope="col">{{ $t("EVALUATION") }}</th>
                            <th scope="col">Artículo</th>
                            <th scope="col">Peso</th>
                            <th scope="col">OK</th>
                            <th scope="col">NOK</th>
                            <th scope="col">NA</th>
                            <th scope="col">Validación</th>
                            <th scope="col">Motivo No</th>
                            <th scope="col">Papá tumba</th>
                        </tr>
                    </thead>
                    <tbody v-for="(item, index) in listDataFile.slice(1)" :key="item">
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
                            <td>{{ item.__EMPTY_9 }}</td>
                            <td>{{ item.__EMPTY_10 }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="row mt-5">
        <div class="btn-showcase text-center">
            <button v-if="type == 1" class="btn btn-pill btn-primary-gradien" type="button"
                @click="importItemEvaluations()">
                {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
            </button>
            <button v-if="type == 2" class="btn btn-pill btn-primary-gradien" type="button"
                @click="importItemEvaluations()">
                {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT_REMOVE") }}
            </button>
        </div>
    </div>
</template>