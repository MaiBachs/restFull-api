<script setup>
import { reactive, ref ,onMounted, watch, computed} from "vue";

// import axios from "axios";
import moment from "moment";

import Multiselect from "@vueform/multiselect";
import VueDatePicker from "@vuepic/vue-datepicker";
import { getChannelType } from "@/store/getChannelType";
import useChannelManager from "@/composables/useChannelManager"
import { useUserStore } from '../../store/UserStore';
import ModalQuestion from '@/components/common/questionModal.vue'
import ErrorModal from '@/components/common/errorModal.vue'
import Modal from '@/components/common/customModal.vue'
import PopupViewChannelApprove from '@/components/channelManager/widgets/popupViewLevel2.vue'
import selectTemplate from "@/components/channelManagerBranch/widgets/selectTemplateImport.vue"
import TableChannelBranch from "@/components/channelManagerBranch/widgets/tableChannelBrach.vue"

const userStore = useUserStore()


const  { exportDetailChannel,exportSearchResult, searchChannel , listChannelLevel2, downloadFile,checkDataSearchChannel, statusApprovedDecode, searchTargetSubmit, submitChannelBranch} = useChannelManager(ref);
const getListChannelType = getChannelType();

const checkListSearch = ref(true);
const year = ref(new Date().getFullYear());
const selectedBranch = ref("")
const bc_code = ref("")
const br_code = ref("")
const fileData = ref("")
const quantitySuccess = ref("");
const showPopupViewChannelApprove = ref(false);
const questionImport = ref(false)
const popupImportResults = ref(false)
const statusApprove = ref("")
const showButtonYear = ref(false)
const newDate = ref(new Date())

const searchData = reactive({
  br_code: "",
  bc_code: "",
  channel_id: "",
  plan_date: "",
  target_level: 2,
});
searchData.plan_date = year;

onMounted(async () => {
  console.log(userStore.currentUser.staff_code )
  if (userStore.currentUser.staff_code !== 'Loadding') {
    await userStore.fetchDataGetBranch();
    
  }
});

watch(
  () => userStore.listBranch,
  async (newVal) => {
    await userStore.getTotalTargetChannel(searchData);
    if (newVal && newVal.length) {
      br_code.value = newVal[0].value;
      // Check if userStore.listBC has data
      if (userStore.listBC && userStore.listBC.length > 0) {
        bc_code.value = userStore.listBC[0].value;
      } else {
        // Wait for userStore.listBC to be populated
        const unwatch = watch(
          () => userStore.listBC,
           (newBCVal) => {
            if (newBCVal && newBCVal.length) {
              bc_code.value = newBCVal[0].value;
              
              unwatch(); // Stop watching once the data is available
            }
          }
        );
      }
    }
  }
);


console.log(userStore)
const downloadFileTemplate = async () => {
  downloadFile(`Template_import_channel_target_br_develop_new.xls`);
};

const getStatusStyle = computed(() => {
  const defaultStyle = {}; // Define your default style here
  
  if (!userStore.totalTargetType.length) return defaultStyle;
  
  const childStatus = userStore.totalTargetType[0].child_status;

  if (childStatus == 1 || childStatus == 2) {
    return { 'background': '#F5DEB3' };
  } else if (childStatus == 3) {
    return { 'background': '#4CAF50', 'color': '#fff' };
  } else if (childStatus == 4) {
    return { 'background': '#4CAF50', 'color': '#fff' };
  } else if (childStatus == 5) {
    return { 'background': 'red', 'color': '#fff' };
  } else if (childStatus == null || !childStatus) {
    return { 'background': '#ffdddd', 'color': '#000' };
  }

  return defaultStyle;
});


async function searchChannelManager() {
  updateSelectedCodeBC(bc_code.value)
  updateSelectedCode(br_code.value)
  checkListSearch.value = true;
  searchData.plan_date = year;
  searchData.target_level = 2
  await searchChannel(searchData)
  console.log(listChannelLevel2.value)
}


// async function viewDetailChannel(br,status) {
//   searchData.br_code = br;
//   statusApprove.value = status
//   searchData.target_level = 2
//   await searchChannel(searchData)
//   showPopupViewChannelApprove.value = true
// }
function exportSearchChannel() {
  updateSelectedCodeBC(bc_code.value)
  updateSelectedCode(br_code.value)
  searchData.target_level = 2
  searchData.plan_date = year;
  exportSearchResult(searchData)
}

function exportDetail() {
  updateSelectedCodeBC(bc_code.value)
  updateSelectedCode(br_code.value)
  searchData.target_level = 2
  searchData.plan_date = year;
  exportDetailChannel(searchData)
}

const showQuestionImport = async () => {
  questionImport.value = true
}

function updateSelectedCodeBC(selectedValue) {
  if(selectedValue) {
    const selectedBC = userStore.listBC.find(bc => bc.value === selectedValue);
    if (selectedBC.value != 7282) {
      searchData.bc_code = selectedBC.code;

    }else {
      searchData.bc_code = ""
    }
  }
  
  }

function updateSelectedCode(selectedValue) {
  if(selectedValue) {
    const selectedBranch = userStore.listBranch.find(br => br.value === selectedValue);
    if (selectedBranch.value != 7282) {
      searchData.br_code = selectedBranch.code;
    }else {
      searchData.br_code = ""
    }
  }
  
}

const  handleDate = async () => {
  showButtonYear.value = false;
  await userStore.getTotalTargetChannel(searchData)
    listChannelLevel2.value = [];
    var year = moment(newDate.value).format("YYYY");
    var planDate = moment(searchData.plan_date).format("YYYY");
    if(planDate >=  year) {
      showButtonYear.value = false;
    } else {
        showButtonYear.value = true;
    }
}


const showPopupSubmit = ref(false)
async function showSubmitChannelBranch() {
  updateSelectedCode(br_code.value)
  await searchTargetSubmit(searchData)
  showPopupSubmit.value = true
}

function submitBranch() {
  submitChannelBranch()
}

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
                          @update:model-value="handleDate"
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
                        >{{ $t("HOME.SEARCH.COMBOBOX.BC") }}:</label
                      >
                      <div class="col-sm-7">
                        <Multiselect
                          v-model="bc_code"
                          :options="userStore.listBC || []"
                          :placeholder="$t('SELECT_ONE')"
                          track-by="code"
                          label="label"
                          :canClear="false"
                          :searchable="false"
                          :allow-empty="false"
                          @input="updateSelectedCodeBC"
                        ></Multiselect>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
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
                        ></Multiselect>
                      </div>
                    </div>
                  </div>
                  <div class="col-md-6"  v-if="!showButtonYear && userStore.listchannelTotal.length">
                    <div class="m-b-25 row">
                      <label class="col-sm-3 col-form-label"
                        >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
                      >
                      <div class="col-sm-8 col-form-label">
                        <div class="col-xs-4" style="padding-left: 10px;">
                          <button id="select_type_import" type="button" class="btn" style="border: 1px solid #ccc;" 
                          data-bs-toggle="modal"
                          data-original-title="test"
                          data-bs-target="#exampleModalSelectTypeImport">
                          {{ $t('CHOOSE_FILE_TYPE') }}</button>
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
                    <button id="submit_branch" v-if="userStore.showSubmit"  type="button" class="btn btn-pill btn-add" style="background-color: #4CAF50;" @click="showSubmitChannelBranch">{{$t('SUBMIT')}}</button>
                  </div>
                </div>
                <div class="row">
                  <div v-if="userStore.listchannelTotal.length" class="col-md-4">
                    <div class="target-plan">
                      <table id="table_sale_target" class="table table-hover target-total-type target-plan-bc">
                          <thead>
                            <tr>
                                <th  class="text-center">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE")}}</th>
                                <th  class="text-center">{{$t("NEW_CHANNEL")}}</th>
                                <th  class="text-center text-color-target-bar">{{$t("CUMULATIVE_CHANNEL")}}</th>
                              
                            </tr>
                          </thead>
                          <tbody>
                            <tr v-for="item in userStore.listchannelTotalTagetType" :key="item" class="target-total-type target-plan-bc" style="border-bottom: 1px solid #ddd;">
                                <td class="text-left" style="text-align: left;">{{item.label}}</td>
                                <td class="text-center" style="font-weight: 600;">{{item['targetType-1'] || 0}}</td>
                                <td class="text-center text-color-target-bar" style="font-weight: 600;">{{item['targetType-2'] || 0}}</td>
                            </tr>
                          </tbody>
                      </table>
                    </div>
                  </div>
                </div>
                
                <div  class="row p-b-20">
                    <div v-if="!userStore.totalTargetTypeAccumulated.length && userStore.totalTargetTypeNewDevelop.length || userStore.totalTargetTypeAccumulated.length && userStore.totalTargetTypeNewDevelop.length" class="col-md-6">
                        <div v-if="userStore.totalTargetType" class="total-status sale-total" style="width: 66%;" :style="getStatusStyle">
                          <div class="target">
                              <strong style="padding-right: 8px;">{{ $t("STATUS")}}:</strong>
                              <p style="margin: 0px;">{{statusApprovedDecode(userStore.totalTargetType[0].child_status) }}</p>
                          </div>
                        </div>
                    </div>
                    
                    <div v-if="!userStore.totalTargetTypeAccumulated.length && !userStore.totalTargetTypeNewDevelop.length" class="col-md-6">
                      <div class="total-status sale-total" style="width: 66%;" :style="getStatusStyle">
                          <div class="target">
                            <strong style="padding-right: 8px;">{{ $t("STATUS")}}:</strong>
                            <p style="margin: 0px;">{{ $t('NO_TARGET_NEW_DEVELOP_ACCUMULATED')}}</p>
                          </div>
                      </div>
                    </div>


                    <div v-if="userStore.totalTargetTypeAccumulated.length && !userStore.totalTargetTypeNewDevelop.length" class="col-md-6">
                      <div class="total-status sale-total" style="width: 66%;"  :style="getStatusStyle">
                          <div class="target">
                            <strong style="padding-right: 8px;">{{ $t("STATUS")}}:</strong>
                            <p style="margin: 0px;">{{$t("TARGET_NEW_DEVELOP")}}</p>
                          </div>
                      </div>
                    </div>

                    <div class="col-md-6" style="float: left;padding-right: 0px;">
                        <div  v-if="userStore.listChannelTargetList && userStore.listChannelTargetList.length" class="total-success">
                          <div class="success">
                              <p style="margin-bottom: 0px;"><strong>T:</strong>{{ $t("TARGET")}}</p>
                          </div>
                          <div class="danger">
                              <p style="margin-bottom: 0px;"><strong style="margin-right: 5px;">P:</strong>{{$t("PLAN")}}</p>
                          </div>
                          <div class="sale-info">
                              <p><strong style="margin-right: 5px;">R:</strong> {{$t("RESULT")}}</p>
                          </div>
                        </div>
                    </div>
                </div>
                
                <div class="row mb-3 evaluations">
                  <fieldset v-if="!checkDataSearchChannel" class="table-responsive">
                    <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
                  </fieldset>
                  <fieldset
                    v-if="listChannelLevel2.length"
                    class="table-responsive"
                  >
                    <TableChannelBranch :listChannelLevel2="listChannelLevel2"  :searchData="searchData"/>
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
  <selectTemplate @clearValue="searchChannelManager" :searchData="searchData" :bcCode="bc_code" :brCode="br_code"/>

  <ModalQuestion v-model="showPopupSubmit" >
    <div class="row">
        <div class="col-sm-12 col-md-12">
            <label class="control-label f-500" style="margin-top: 7px;">
              {{$t('SUBMIT_MESSAGE')}}
            </label>
        </div>
      </div>
      <template #modalActions="modalActionProps">
        <button class="btn btn-primary" type="button" @click="submitBranch(modalActionProps)">
          {{ $t("SUBMIT") }}
        </button>
    </template>
  </ModalQuestion>
</template>
