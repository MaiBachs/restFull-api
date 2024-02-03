<script setup>
import { getChannelType } from "@/store/getChannelType";
import Multiselect from "@vueform/multiselect";
import PopupAssign from "@/components/checklist/popupAssign.vue";
import { useLoaddingStore } from "./../../store/LoaddingStore";
import PopupDeleteAssign from "@/components/checklist/popupDeleteAssign.vue";
import PopupEditAssign from '@/components/checklist/popupEditAssign.vue'
import { notify } from "@kyvg/vue3-notification";
import { i18n } from '../../main.js';
import axios from "axios";
import { ref, reactive } from "vue";
// import ModalCheckList from '@/components/common/customModal.vue'

const loaddingStore = useLoaddingStore();
const getListPlan = getChannelType();
const getListSearchAssignment = ref([]);
const checkListData = ref(true);
const listCheckListParent1 = ref([]);
const listItemCommonDelete = ref([])
const listItemCommonEdit = ref([])
const compareListItemCommonEdit = ref([])
const expands = ref([])
const expandsLevel2 = ref([])
const searchData = reactive({
  plan_id: ""
});
const listNewAssign = ref([]);

// const inputCheckLevel2 = ref(true);
// const showlevel3 = ref(true);

const searchPlanAssignment = async () => {
  getListSearchAssignment.value = [];
  if(searchData.plan_id) {
      checkListData.value = true;
    try {
      loaddingStore.loading = true;
      const response = await axios.post(
        "/api/plan-job/search-assign-checklist",
        searchData
      );
      if (
        response.data &&
        response.data.search_assign_check_list_responses &&
        response.data.search_assign_check_list_responses.length
      ) {
        
        getListSearchAssignment.value =
          response.data.search_assign_check_list_responses;
          
        checkListData.value = true;
      } else {
        checkListData.value = false;
        getListSearchAssignment.value = [];
      }

      loaddingStore.loading = false;
    } catch (error) {
      loaddingStore.loading = false;
      notify({
        type: "error",
        title: "Get current user",
        text: error,
        duration: 30000,
      });
    }
  } else {
    notify({
        type: "error",
        title: "Get current user",
        text: i18n.global.t('PLEASE_SELECt_PLAN'),
        duration: 30000,
      });
  }
  
};
function getCheckListParent1(data,dataEdit) {
  listCheckListParent1.value = []
  listItemCommonDelete.value = []
  listItemCommonEdit.value = []
  listCheckListParent1.value = JSON.parse(JSON.stringify([data]));
  listItemCommonEdit.value = JSON.parse(JSON.stringify([dataEdit]));
  compareListItemCommonEdit.value = JSON.parse(JSON.stringify([dataEdit]));
  listCheckListParent1.value.forEach(item => {
    item.valueDelete = false;
    item.search_assign_check_list_response_lv2.forEach(item2 => {
      item2.valueDelete = false;
      item2.check_list_parent3s.forEach(item3 => {
        item3.valueDelete = false;
      });
    });
  });
  listItemCommonEdit.value.forEach(item => {
    item.search_assign_check_list_response_lv2.forEach(item2 => {
      item2.check_list_parent3s.forEach(item3 => {
        if(item3.required === 1) {
          item3.required = true
        }else if(item3.required === null || item3.required === 0) {
          item3.required = false
        }
        if(item3.required_file === 1) {
          item3.required_file = true
        }else if(item3.required_file === null || item3.required_file === 0) {
          item3.required_file = false
        }
      });
    });
  });
  compareListItemCommonEdit.value.forEach(item => {
    item.search_assign_check_list_response_lv2.forEach(item2 => {
      item2.check_list_parent3s.forEach(item3 => {
        if(item3.required === 1) {
          item3.required = true
        }else if(item3.required === null || item3.required === 0) {
          item3.required = false
        }
        if(item3.required_file === 1) {
          item3.required_file = true
        }else if(item3.required_file === null || item3.required_file === 0) {
          item3.required_file = false
        }
      });
    });
  });
}


function clearListPlanJobId() {
  loaddingStore.loading = true;
  searchPlanAssignment()
}

function changeInputLevel1(id) {
  if (expands.value.includes(id)) {
    expands.value = expands.value.filter(o => o != id)
  } else {
    expands.value.push(id)
  }
}
function changeInputLevel2(id) {
  if (expandsLevel2.value.includes(id)) {
    expandsLevel2.value = expandsLevel2.value.filter(o => o != id)
  } else {
    expandsLevel2.value.push(id)
  }
}
function clearSelectPlan() {
  searchData.plan_id = null
  getListSearchAssignment.value = []
}
function selectPlanId() {
  getListSearchAssignment.value = []
  checkListData.value = true
}

// const isOpen = ref(false)
// const isOpen2 = ref(false)
// function showPopup() {
//   isOpen.value = true
// }
// function openModal2() {
//   isOpen2.value = true
// }

</script>
<template>
  <div class="card-body p-0">
    <div class="row mb-3">
      <div class="col align-self-start">
        <div class="mb-3 row form-group">
          <label class="col-sm-4 col-form-label">{{ $t("PLAN") }}:</label>
          <div class="col-sm-8">
            <Multiselect
              class="form-control"
              v-model="searchData.plan_id"
              @input="selectPlanId"
              @clear="clearSelectPlan"
              :options="getListPlan.getListFillPlan || []"
              track-by="name"
              :placeholder="$t('SELECT_ONE')"
            ></Multiselect>
          </div>
        </div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row"></div>
      </div>
      <div class="col align-self-start">
        <div class="mb-3 row"></div>
      </div>
    </div>
    <div class="row mb-3">
      <div class="btn-showcase text-center">
        <button
          class="btn btn-pill btn-primary"
          type="button"
          @click="searchPlanAssignment"
        >
          {{ $t("SEARCH") }}
        </button>
        <button
          :disabled="!searchData.plan_id"
          class="btn btn-pill btn-add"
          type="button"
          data-bs-toggle="modal"
          data-original-title="test"
          data-bs-target="#popupShowAssign"
        >
        {{ $t("DO_ASSIGNMENT_TO_CHECKLIST") }}
        </button>

      <!-- <button
        @click="showPopup"
        class="btn btn-danger btn-xs"
        style="margin-right: 10px"

      >
      Test Popup
      </button> -->
      </div>
    </div>

    <div class="row p-15">
      <fieldset v-if="!checkListData" class="table-responsive">
        <div class="text-center no-item">{{ $t("NO.ITEM") }}</div>
      </fieldset>
      <div v-if="getListSearchAssignment?.length">
        <table class="table table-bordered table-hover">
          <thead class="table-success">
            <tr>
              <th scope="col" style="width: 60%;">{{ $t("MENU/SUBMENU") }}</th>
              <th scope="col">{{ $t("REQUIRE") }}</th>
              <th scope="col">{{ $t("FILE_REQUIRE") }}</th>
              <th scope="col">{{ $t("FILE_TYPE") }}</th>
              <th scope="col">Index<span class="lable-require">*</span></th>
              <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
            </tr>
          </thead>
          <tbody v-for="data in getListSearchAssignment" :key="data">
            <tr>
              <td class="list_parent1">
                <div class="" @click="changeInputLevel1(data.check_list_parent_id1)">
                  <i v-if="expands.includes(data.check_list_parent_id1)" class="fa fa-plus-square-o"></i>
                  <i v-if="!expands.includes(data.check_list_parent_id1)" class="fa fa-minus-square-o"></i>
                  {{ data.check_list_parent1}}(<span style="font-size: 13px;color: darkviolet;">{{ data.check_list_parent_code1 }}</span>)
                </div>
                
              </td>
              <td></td>
              <td></td>
              <td></td>
              <td class="check-icon">{{ data.idx1 }}</td>
              <td style="display: flex">
                <button
                  @click="getCheckListParent1(data,data)"
                  class="btn btn-danger btn-xs"
                  style="margin-right: 10px"
                  data-bs-toggle="modal"
                  data-original-title="test"
                  data-bs-target="#popupShowDeleteAssign"

                >
                  <i class="icofont icofont-ui-delete"></i>
                </button>
                <button class="btn btn-primary btn-xs"
                  @click="getCheckListParent1(data,data)"
                  style="margin-right: 10px"
                  data-bs-toggle="modal"
                  data-original-title="test"
                  data-bs-target="#popupShowEditAssign"
                  >
                  <i class="icofont icofont-edit"></i>
                </button>
              </td>
            </tr>
            
            <template
                
                v-for="lv2 in data.search_assign_check_list_response_lv2"
                :key="lv2.check_list_parent_id2"
              >
                <tr v-if="!expands.includes(data.check_list_parent_id1)">
                  <td class="list_parent2">
                    <div class="" @click="changeInputLevel2(lv2.check_list_parent_id2)">
                      <i v-if="expandsLevel2.includes(lv2.check_list_parent_id2)" class="fa fa-plus-square-o"></i>
                      <i v-if="!expandsLevel2.includes(lv2.check_list_parent_id2)" class="fa fa-minus-square-o"></i>
                      <!-- {{ lv2.check_list_parent2 +'('+ lv2.check_list_parent_code2 + ')' }} -->
                      {{ lv2.check_list_parent2}}(<span style="font-size: 13px;color: darkviolet;">{{ lv2.check_list_parent_code2 }}</span>)
                    </div>

                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td class="check-icon">{{ lv2.idx2 }}</td>
                  <td></td>
                </tr>
                <template v-if="!expands.includes(data.check_list_parent_id1) && !expandsLevel2.includes(lv2.check_list_parent_id2)">
                  <tr
                  v-for="lv3 in lv2?.check_list_parent3s || []"
                  :key="lv3"
                >
                  <td  class="list_parent3">
                    {{ lv3.job_name}}(<span style="font-size: 13px;color: darkviolet;">{{ lv3.job_name }}</span>)
                    <!-- {{ lv3.job_name +'('+ lv3.job_name + ')' }} -->
                    </td>
                  <td  class="check-icon">
                    <i v-if="lv3.required == 1" class="fa fa-check-square-o"></i>
                    <p v-if="lv3.required == 0"></p>
                  </td>
                  <td  class="check-icon">
                    <i
                      v-if="lv3.required_file == 1"
                      class="fa fa-check-square-o"
                    ></i>
                    <p v-if="lv3.required_file == 0"></p>
                  </td>
                  <td  class="check-icon">
                    <p v-if="lv3.file_type == 1">Photo</p>
                    <p v-if="lv3.file_type != 1"></p>
                  </td>
                  <td  class="check-icon">{{ lv3.idx }}</td>
                  <td></td>
                </tr>
                </template>
                
              </template>
          </tbody>
        </table>
      </div>
    </div>

    <PopupAssign  @clearValue="clearListPlanJobId" v-model="listNewAssign" :item="searchData.plan_id" />
    <PopupDeleteAssign  @clearValue="clearListPlanJobId" :listItemDelete="listCheckListParent1" :listItemCommonDelete="listItemCommonDelete"/>
    <PopupEditAssign  @clearValue="clearListPlanJobId"  v-model="listItemCommonEdit" :compareListItemCommonEdit="compareListItemCommonEdit"/>

    <!-- <ModalCheckList v-model="isOpen">
      <div>
        Hello Test
        <button class="btn btn-success" @click="openModal2">Open modal 2</button>
      </div>
    </ModalCheckList>

    <ModalCheckList v-model="isOpen2">
      <div>
        Hello Test 2
      </div>
    </ModalCheckList> -->

  </div>
</template>
<style scoped>

</style>
