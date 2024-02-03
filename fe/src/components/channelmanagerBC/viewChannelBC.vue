<script setup>
import { reactive, ref ,onMounted, watch} from "vue";
import moment from 'moment'
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
import TableChannelBC from "@/components/channelManagerBranch/widgets/tableChannelBC.vue"
import TableListTotalBc from "../channelManagerBranch/widgets/TableListTotalBc.vue";

const userStore = useUserStore()
const newDate =  ref(new Date())
const  { exportDetailChannel,exportSearchResult, searchChannel , listChannelLevel3, downloadFile,checkDataSearchChannel} = useChannelManager(ref);
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
const searchData = reactive({
  br_code: "",
  bc_code: "",
  staff_code: "",
  channel_id: "",
  plan_date: "",
  target_level: 3,
});
searchData.plan_date = year;
const staff_id = ref("")
onMounted(async () => {
  if (userStore.currentUser.staff_code !== 'Loadding') {
    await userStore.fetchDataGetBranch();
  }
});
watch(
  () => userStore.listBranch,
  async (newVal) => {
    if (userStore.currentUser.shop_type == "BR") {
        searchData.checkBRBC = true
      }
      await userStore.getTotalTargetChannel(searchData)
    if (newVal && newVal.length) {
      br_code.value = newVal[0].value;
      console.log(userStore.listBC)
      if (userStore.listBC && userStore.listBC.length) {
        bc_code.value = userStore.listBC[0].value;
      }else {
        userStore.listBC = userStore.listBranch
        bc_code.value = userStore.listBranch[0].value
        await userStore.getStaff(bc_code.value);
        staff_id.value = userStore.listStaff[0].value;
      }
      if (userStore.listStaff && userStore.listStaff.length > 0) {
        staff_id.value = userStore.listStaff[0].value;
      } else {
        const unwatch = watch(
          () => userStore.listStaff,
          async (newStaffVal) => {
            
            if (newStaffVal && newStaffVal.length) {
              staff_id.value = newStaffVal[0].value;
              unwatch();
            }
          }
        );
      }
    }
    
  }
  
);


const downloadFileTemplate = async () => {
  downloadFile(`Template_import_channel_target_bc_develop_new.xls`);
};


async function searchChannelManager() {
  updateSelectedCodeBC(bc_code.value)
  updateSelectedCode(br_code.value)
  checkListSearch.value = true;
  searchData.plan_date = year;
  searchData.target_level = 3
  await searchChannel(searchData)
}


function exportSearchChannel() {
  updateSelectedCodeBC(bc_code.value)
  updateSelectedCode(br_code.value)
  searchData.target_level = 3
  searchData.plan_date = year;
  exportSearchResult(searchData)
}

function exportDetail() {
  updateSelectedCodeBC(bc_code.value)
  updateSelectedCode(br_code.value)
  searchData.target_level = 3
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
    listChannelLevel3.value = [];
    var year = moment(newDate.value).format("YYYY");
    var planDate = moment(searchData.plan_date).format("YYYY");
    if(planDate >=  year) {
      showButtonYear.value = false;
    } else {
        showButtonYear.value = true;
    }
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
                          disabled
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
                          disabled
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
                        >{{ $t("HOME.SEARCH.COMBOBOX.ZONAL") }}:</label
                      >
                      <div class="col-sm-7">
                        <Multiselect
                          v-model="staff_id"
                          :options="userStore.listStaff || []"
                          :placeholder="$t('SELECT_ONE')"
                          track-by="label"
                          label="label"
                          :searchable="false"
                          :allow-empty="false"
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
                        ></Multiselect>
                      </div>
                    </div>
                  </div>
          
                </div>
                <div v-if="showButtonYear && userStore.listchannelTotal && userStore.listchannelTotal.length && userStore.showChoseFileBC" class="row m-t-25">
                   <div class="col-md-6">
                    <div class="m-b-25 row">
                      <label class="col-sm-3 col-form-label"
                        >{{ $t("VISIT_PLAN.IMPORT.LABEL.FILE") }}:</label
                      >
                      <div class="col-sm-8 col-form-label">
                        <div class="col-xs-4">
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
                  </div>
                </div>
                <div class="row p-15">
                  <div class="col-md-6 p-l-0">
                    <div v-if="!userStore.showChoseFileBC  && !userStore.totalTargetTypeAccumulated.length" class="total-status sale-total" style="width: 70%; ;background:#ffdddd; color: #000">
                      <div class="target">
                          <strong style="padding-right: 8px;font-size: 14px;">{{$t("STATUS")}}:</strong>
                          <p style="margin: 0px;">{{$t("NO_TARGET_NEW_DEVELOP_ACCUMULATED")}}</p>
                      </div>
                    </div>
                    <div v-if="!userStore.showChoseFileBC && userStore.totalTargetTypeAccumulated && userStore.totalTargetTypeAccumulated.length" class="total-status sale-total" style="background:#ffdddd; color: #000">
                      <div class="target">
                          <strong style="padding-right: 8px;font-size: 14px;">{{ $t("STATUS")}}:</strong>
                          <p style="margin: 0px;">{{ $t("TARGET_NEW_DEVELOP")}}</p>
                      </div>
                    </div>
                  </div>
                  <div class="col-md-6">
                      <div v-if="userStore.listchannelTotal && userStore.listchannelTotal.length && userStore.showChoseFileBC || listChannelLevel3 && listChannelLevel3.length" class="total-success">
                        <div v-if="userStore.listchannelTotal && userStore.listchannelTotal.length && userStore.showChoseFileBC" class="accumulated" style="margin: 0px 5px 8px 5px;">
                          <p style="margin-bottom: 0px;"><strong style="margin-right: 5px;">N:</strong>{{ $t("DEVELOP_NEW")}}</p>
                        </div>

                        <div v-if="userStore.listchannelTotal && userStore.listchannelTotal.length && userStore.showChoseFileBC" class="accumulated develop-new">
                          <p style="margin-bottom: 0px;"><strong  style="margin-right: 5px;">A:</strong>{{ $t("ACCUMULATED")}}</p>
                        </div>
                      
                        <div class="success">
                          <p style="margin-bottom: 0px;"><strong>T:</strong>{{ $t("TARGET")}}</p>
                        </div>
                        <div class="danger">
                          <p style="margin-bottom: 0px;"><strong style="margin-right: 5px;">P:</strong>{{ $t('PLAN')}}</p>
                        </div>
                        <div class="sale-info">
                          <p><strong style="margin-right: 5px;">R:</strong> {{ $t("RESULT")}}</p>
                        </div>
                      </div>
                    </div>
                </div>
               
                <div>
                  <TableListTotalBc :listchannelTotalTagetType="userStore.listchannelTotalTagetType" :totalTargetTypeAccumulated="userStore.totalTargetTypeAccumulated" :showChoseFileBC="userStore.showChoseFileBC"/>
                </div>
                
                
                <div class="row mb-3 evaluations">
                  <fieldset v-if="!checkDataSearchChannel" class="table-responsive">
                    <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
                  </fieldset>
                  <fieldset
                    v-if="listChannelLevel3.length"
                    class="table-responsive"
                  >
                    <TableChannelBC :listChannelLevel3="listChannelLevel3" :searchData="searchData"/>
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
</template>
<style scoped>


  
</style>
