<script setup>
import { reactive, ref ,onMounted, watch} from "vue";
import { i18n } from "../../main.js";
// import axios from "axios";
// import moment from "moment";
import { useLoaddingStore } from "../../store/LoaddingStore";
import { notify } from "@kyvg/vue3-notification";
import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import { getChannelType } from "@/store/getChannelType";
import useChannelManager from "@/composables/useChannelManager"
import { useUserStore } from './../../store/UserStore';
import ModalQuestion from '@/components/common/questionModal.vue'
import ErrorModal from '@/components/common/errorModal.vue'
import Modal from '@/components/common/customModal.vue'
import PopupViewChannelApprove from '@/components/channelManager/widgets/popupViewLevel2.vue'
const userStore = useUserStore()

const  { exportDetailChannel,exportSearchResult,importChannelManager, searchChannel, importResults, listChannelLevel1 , statusApprovedDecode, listChannelLevel2, downloadFile, downloadFileError} = useChannelManager(ref);
const br_code = ref("")
const getListChannelType = getChannelType();
const loaddingStore = useLoaddingStore();
const totalRecord = ref(0);
const checkListSearch = ref(true);
// const year = ref(new Date().getFullYear());
const year = ref();
const selectedBranch = ref("")
const successMessageFlag = ref(false);
const errorMessageFlag = ref(false);
const fileNameError = ref("");
const fileData = ref("")
const inputFile = ref(null);
const quantitySuccess = ref("");
const showPopupViewChannelApprove = ref(false);
const questionImport = ref(false)
const popupImportResults = ref(false)
const statusApprove = ref("")

const searchData = reactive({
  br_code: "",
  channel_id: "",
  plan_date: "",
  target_level: 1,
});
searchData.plan_date = year;

onMounted(async () => {
  if (userStore.currentUser.staff_code !== 'Loadding') {
    await userStore.fetchDataGetBranch();
  }
});

watch(() => userStore.listBranch, (newVal) => {
  if (newVal && newVal.length) {
    br_code.value = newVal[0].value;
  }
});
const downloadFileTemplate = async () => {
  downloadFile(`Template_import_channel_target_sale.xls`);
};
function updateSelectedCode(selectedValue) {
  const selectedBranch = userStore.listBranch.find(br => br.value === selectedValue);
    if (selectedBranch.value != 7282) {
      searchData.br_code = selectedBranch.code;
    }else {
      searchData.br_code = ""
    }
}


async function searchChannelManager() {
  updateSelectedCode(br_code.value)
  checkListSearch.value = true;
  searchData.plan_date = year;
  searchData.target_level = 1
  searchChannel(searchData)
}


async function viewDetailChannel(br,status) {
  searchData.br_code = br;
  statusApprove.value = status
  searchData.target_level = 2
  await searchChannel(searchData)
  showPopupViewChannelApprove.value = true
}
function exportSearchChannel() {
  updateSelectedCode(br_code.value)
  searchData.target_level = 1
  searchData.plan_date = year;
  exportSearchResult(searchData)
}

function exportDetail() {
  updateSelectedCode(br_code.value)
  searchData.target_level = 1
  searchData.plan_date = year;
  exportDetailChannel(searchData)
}

const previewFiles = async () => {
  fileData.value = "";
  fileNameError.value = "";
  successMessageFlag.value = false;
  errorMessageFlag.value = false;
  fileData.value = inputFile.value.files[0];
}

const showQuestionImport = async () => {
  questionImport.value = true
}

const importChannel = async () => {
  updateSelectedCode(br_code.value)
  searchData.target_level = 1
  await importChannelManager(searchData,fileData)
  if( importResults.value && importResults.value.data.status && importResults.value.data.status.message ) {
      popupImportResults.value = true
      errorMessageFlag.value = true
      quantitySuccess.value = importResults.value.data.status.code
      fileNameError.value = importResults.value.data.status.message
    } else {
      questionImport.value = false
      inputFile.value.value = null;
      searchChannelManager()
      notify({
        type: "success",
        title: i18n.global.t("VISIT_PLAN.IMPORT.LABEL.IMPORT"),
        text: importResults.value.data.status.code,
        duration: 3000,
      });
      loaddingStore.loading = false;
    }
  
}
const downloadErrorImmportFile = () => {
  
  downloadFileError(fileNameError.value)
};

</script>
<template>
  <div class="widgets">
    <div class="row">
      <div class="col-sm-12">
        <div class="card">
          <div class="card-header pb-0">
            <h5>{{ $t("CHANNEL_MANAGER") }}</h5>
          </div>
          <div class="card-body">
            <div class="tabbed-card">
              <div class="card-body p-0">
                <div class="row m-b-25">
                  <div class="col-md-6">
                    <div class="m-b-5 row">
                      <label class="col-sm-3 col-form-label"
                        >{{ $t("DATE") }}:</label
                      >
                      <div class="col-sm-7">
                        <VueDatePicker
                          v-model="year"
                          auto-apply
                          year-picker
                          format="yyyy"
                        ></VueDatePicker>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-6 download-template">
                    <a @click="downloadFileTemplate">{{  $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOAD_TEMPLATE") }}</a>
                </div>
                  
                </div>


                <div class="row">
                  <div class="col-md-6">
                    <div class="m-b-25 row">
                      <label class="col-sm-3 col-form-label"
                        >{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:</label
                      >
                      <div class="col-sm-7">
                        <Multiselect
                          v-model="br_code"
                          :options="userStore.listBranch || []"
                          track-by="code"
                          label="label"
                          @clear="clearDataBranch"
                          :canClear="false"
                          :value="selectedBranch"
                          @input="updateSelectedCode"
                          :placeholder="$t('SELECT_ONE')"
                        ></Multiselect>
                      </div>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="m-b-5 row">
                      <label class="col-sm-3 col-form-label"
                        >{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:</label
                      >
                      <div class="col-sm-7">
                        <Multiselect
                          v-model="searchData.channel_id"
                          :options="getListChannelType.listChannelType || []"
                          :placeholder="$t('SELECT_ONE')"
                          track-by="label"
                          label="label"
                          :searchable="false"
                          :allow-empty="false"
                          @select="handleChannelTypeChange"
                        ></Multiselect>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-6">
                    <div class="m-b-25 row">
                      <label class="col-sm-3 col-form-label"
                        >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
                      >
                      <div class="col-sm-8 col-form-label">
                        <div class="">
                            <form>
                            <input
                            id="file"
                            ref="inputFile"
                            accept=".xls, .xlsx"
                            type="file"
                            @change="previewFiles"
                              
                            />
                            </form>
                        </div>
                      </div>
                    </div>
                  </div>
                 
                </div>
                <div class="row p-25">
                  
                  <div class="btn-showcase text-center">
                    <button
                      class="btn btn-pill btn-primary"
                      type="button"
                      @click="searchChannelManager"
                    >
                      {{ $t("SEARCH") }}
                    </button>
                    <button
                      style="background-color: #2dacd1"
                      class="btn btn-pill btn-add"
                      type="button"
                      @click="exportSearchChannel"
                     
                    >
                    {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
                    </button>
                    <button
                      class="btn btn-pill btn-add"
                      type="button"
                      @click="exportDetail"
                    >
                      {{ $t("EXPORT_DETAIL") }}
                    </button>
                    
                    <button
                      v-if="fileData"
                      type="button"
                      class="btn btn-pill btn-add"
                      style="background-color: #4caf50"
                      @click="showQuestionImport"
                    >
                      {{ $t("VISIT_PLAN.IMPORT.LABEL.IMPORT") }}
                    </button>
                  </div>
                </div>
                <div class="row mb-3 evaluations">
                  <fieldset v-if="!checkListSearch" class="table-responsive">
                    <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
                  </fieldset>
                  <fieldset
                    v-if="listChannelLevel1.length"
                    class="table-responsive"
                  >
                    <div class="table-responsive">
                      <table class="table table-bordered table-hover">
                        <thead
                          class="table-success"
                          style="background-color: #24695c; color: #fff"
                        >
                          <tr>
                            <th scope="col">
                              {{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}
                            </th>
                            <th scope="col">{{ $t("STATUS") }}</th>
                            <th scope="col">
                              {{
                                $t(
                                  "HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE"
                                )
                              }}
                            </th>
                            <th scope="col">{{ $t("TARGET") }}</th>
                            <th scope="col">{{ $t("PLAN") }}</th>
                            <th scope="col">{{ $t("RESULT") }}</th>
                            <th scope="col">
                              {{ $t("HOME.SEARCH.MAP.ACTION") }}
                            </th>
                          </tr>
                        </thead>
                        <tbody
                          v-for="item in listChannelLevel1"
                          :key="item"
                        >
                          <tr>
                            <td
                              :rowspan="item.channels.length + 1"
                              class="text-left"
                              style="vertical-align: middle; text-align: center"
                            >
                              {{ item.br_code }}
                            </td>
                            <td
                              :rowspan="item.channels.length + 1"
                              v-if="item.br_code != 'Total'"
                              style="vertical-align: middle; text-align: center"
                              class="text-center plan-sale-td label-item"
                            >
                              <span
                                @click="
                                  viewDetailChannel(
                                    item.br_code,
                                    item.child_status
                                  )
                                "
                                style="
                                  color: cadetblue;
                                  text-decoration: underline;
                                "
                                >{{
                                  statusApprovedDecode(item.child_status)
                                }}</span
                              >
                            </td>
                            <td
                              :rowspan="item.channels.length + 1"
                              v-if="item.br_code == 'Total'"
                              class="text-channel-code"
                              style="vertical-align: middle; text-align: center"
                            ></td>
                          </tr>
                          <tr
                            v-for="channel in item.channels"
                            :key="channel"
                            :class="{
                              'text-color-target': channel.target_type == 2,
                            }"
                            :style="
                              item.br_code == 'Total' && {
                                background: 'antiquewhite',
                                'font-weight': '700',
                              }
                            "
                            class="no-top-border"
                            style="border-bottom: 1px solid #ddd"
                          >
                            <td
                              :class="{
                                'text-color-target': channel.target_type == 2,
                              }"
                              class="text-channel-code"
                            >
                              {{ channel.channel_code }}
                            </td>
                            <td>{{ channel.target.total || 0 }}</td>
                            <td v-if="channel.target_type == 1">
                              {{ channel.plan.total || 0 }}
                            </td>
                            <td v-if="channel.target_type == 2">-</td>
                            <td>{{ channel.result.total || 0 }}</td>
                            <td
                              v-if="
                                item.br_code != 'Total' &&
                                channel.channel_id == 101052599 &&
                                channel.target_type != 2
                              "
                            >
                              <button
                                @click="
                                  showDetailDateChannelSale(
                                    item.br_code,
                                    channel.channel_id,
                                    channel.targetId
                                  )
                                "
                                style="
                                  border: none;
                                  background: transparent;
                                  color: darkcyan;
                                  text-decoration-line: underline;
                                "
                              >
                                {{ $t("DETAIL_FOR_DF") }}
                              </button>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                      <div class="pagination-search">
                        <pagination
                          v-model="searchData.page"
                          :records="totalRecord"
                          :per-page="20"
                          @paginate="myCallback"
                        />
                      </div>
                    </div>
                  </fieldset>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <ModalQuestion v-model="questionImport" :title="$t('CONFIRM')">
    <div class="text-center p-10">{{ $t("IMPORT.LABEL.CONFIRM") }}</div>
    <template #modalActions="modalActionProps">
        <button class="btn btn-primary" type="button" @click="importChannel(modalActionProps)">
          {{ $t("SUBMIT") }}
        </button>
    </template>
  </ModalQuestion>
  

  <ErrorModal v-model="popupImportResults" :title="$t('TARGET_RESULTS')">
    <div class="text-center p-10">
      <label>{{quantitySuccess}}</label>
    </div>
    <template #modalActionsError="modalActionsErrorProps">
        <button class="btn btn-danger" type="button" @click="downloadErrorImmportFile(modalActionsErrorProps)">
          {{ $t("VISIT_PLAN.IMPORT.LABEL.DOWNLOADEXCEL") }}
        </button>
    </template>
  </ErrorModal>

  <Modal v-model="showPopupViewChannelApprove" :title="$t('CHANNEL_MANAGEMENT')">
      <PopupViewChannelApprove :listChannelLevel2= "listChannelLevel2" :statusApprove="statusApprove"/>
  </Modal>
</template>
<style scoped>
th {
  font-size: 12px;
  white-space: nowrap;
}
td {
  font-size: 13px;
}

.text-channel-code.text-color-target {
  padding-left: 15px !important;
}
</style>
