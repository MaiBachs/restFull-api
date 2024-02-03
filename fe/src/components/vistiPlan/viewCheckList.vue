<script setup>
import { defineProps, ref,defineEmits } from "vue";
import axios from "axios";
import { i18n } from '../../main.js';
import { notify } from "@kyvg/vue3-notification";
import ApproveModal from '@/components/common/approveModal.vue' 
import ErrorModal from '@/components/common/errorModal.vue'
import Modal from '@/components/common/customModal.vue'
import PopupShowImage from '@/components/vistiPlan/popupShowImage.vue'
import { useLoaddingStore } from "@/store/LoaddingStore";
import { useUserStore } from '@/store/UserStore';
const userStore = useUserStore()
const loaddingStore = useLoaddingStore();
const emit = defineEmits(['update:modelValue'])

const selectedTab = ref(0);
const showPopupCheckListApproved =  ref(false)
const showPopupCheckListReject =  ref(false)
const commentApprove = ref("")
const commentReject = ref("")
const filebase64VisitPlan = ref([])
const getlistBase64Image = ref([])
const listImage = ref(false)

const props = defineProps({
  listCateResult: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  checkListCateResult: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  checklistrequired: {
    type: Boolean,
    required: true,
    default: false
  },
  plan: {
    type: Object,
    required: true,
    default: () => ({}),
  }
});
function selectTab(index) {
  selectedTab.value = index;
}

function showViewApproved() {
  showPopupCheckListApproved.value = true
}
function showViewReject() {
  showPopupCheckListReject.value = true
}

async function viewChannelApproved() {
  const params = {
        visit_plan_id: props.plan.id,
        check_list_result_status: 1,
        check_list_result_comment: commentApprove.value || "",
    };
    try {
      loaddingStore.loading = true;
      const response = await axios.put("/api/visit-plan-result/set-status", params);
      if(response.data && response.data.data) {
        showPopupCheckListApproved.value = false
        emit('clearValue')
      }
      loaddingStore.loading = false;
    } catch (error) {
      loaddingStore.loading = false;
      notify({
        type: "error",
        title: "Get current user",
        text: error,
        duration: 500,
      });
    }

}
async function reject() {
  if(commentReject.value) {
    const params = {
        visit_plan_id: props.plan.id,
        check_list_result_status: -1,
        check_list_result_comment: commentReject.value || "",
    };
    try {
      loaddingStore.loading = true;
      const response = await axios.put("/api/visit-plan-result/set-status", params);
      console.log(response.data.data)
      if(response.data && response.data.data) {
        showPopupCheckListReject.value = false
        emit('clearValue')
      }
      loaddingStore.loading = false;
    } catch (error) {
      loaddingStore.loading = false;
      notify({
        type: "error",
        title: "Get current user",
        text: error,
        duration: 500,
      });
    }
  }else {
    notify({
        type: "warning",
        text: i18n.global.t("PLEASE_COMMENT"),
        duration: 500,
      });
    return;
  }

}

async function getImage(url, totalRequest) {
  try {
      loaddingStore.loading = true;
      const response  = await axios.get(`/api/item-config/getImageFromFtp?filePath=${url}`);
      if(response.data && response.data.data) {
        filebase64VisitPlan.value.push(response.data?.data || {content: ''})
        console.log(filebase64VisitPlan.value)
        console.log(totalRequest)
        if (totalRequest == filebase64VisitPlan.value.length) {
            listImage.value = true
        }
      }
      
      loaddingStore.loading = false;
    } catch (error) {
      loaddingStore.loading = false;
      notify({
        type: "error",
        title: "Get current user",
        text: error,
        duration: 500,
      });
    }
  }

  function viewImage(filePath) {
      filebase64VisitPlan.value = [];
      getlistBase64Image.value = [];
      filePath.map(async img => {
          return await getImage(img, filePath.length)
      });
  }


</script>
<template>
  <div class="col-xs-12">
    <div
      class="modal-body"
      style="margin: 5px 0px 20px 0px; padding-bottom: 0px; padding-top: 5px"
    >
      <div v-if="props.checkListCateResult == false" class="modal-check-list">
        <div class="tabs">
          <ul class="nav-tabs">
            <li
              v-for="(tab, index) in props.listCateResult"
              :key="tab.job_id"
              @click="selectTab(index)"
              :class="{ active: selectedTab === index }"
            >
              {{ tab.code }}
            </li>
          </ul>
        </div>
        <div class="tab-content">
          <div v-if="selectedTab !== null">
            <div
              v-for="level2 in props.listCateResult[selectedTab].sub_items"
              :key="level2"
            >
              <p
                style="
                  background: #afeeee;
                  padding: 10px;
                  margin-bottom: 0px;
                  margin-top: 10px;
                "
              >
                {{ level2.name }}
              </p>

              <table
                v-if="level2.sub_items.length"
                class="table table-hover table-bordered table-hover"
              >
                <thead>
                  <tr class="black-muted-bg">
                    <th scope="col" class="align-left">
                      {{ $t("HOME.SEARCH.MAP.ACTION.STOCK_INFO.NAME") }}
                    </th>
                    <th
                      scope="col"
                      class="align-left col-result"
                      style="width: 20%"
                    >
                      {{ $t("RESULT") }}
                    </th>
                    <th
                      scope="col"
                      class="align-left col-reason"
                      style="width: 30%"
                    >
                      {{ $t("Reason") }}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="level3 in level2.sub_items"
                    :key="level3"
                    class="no-top-border bg-secondary"
                  >
                    <td class="text-left">
                      {{ level3.name
                      }}<span
                        v-if="level3.require_file == 1"
                        style="color: red; margin-left: 5px"
                        >(*)</span
                      >
                    </td>
                    <td class="text-left">
                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 0 &&
                          !level3.list_file_path.length &&
                          level3.result == 'true'
                        "
                      >
                        <p class="list-ok">OK</p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 0 &&
                          level3.list_file_path.length &&
                          level3.result == 'true'
                        "
                      >
                        <p class="list-ok">OK</p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list"
                        v-if="
                          level3.require_file == 0 &&
                          !level3.list_file_path.length &&
                          level3.result == 'false'
                        "
                      >
                        <p class="list-ok">NOT OK</p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 0 &&
                          level3.list_file_path.length &&
                          level3.result == 'false'
                        "
                      >
                        <p class="list-ok">OK</p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list"
                        v-if="
                          level3.require_file == 0 &&
                          !level3.list_file_path.length &&
                          level3.result == null
                        "
                      >
                        <p class="list-ok"></p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 0 &&
                          level3.list_file_path.length &&
                          level3.result == null
                        "
                      >
                        <p class="list-ok"></p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list"
                        v-if="
                          level3.require_file == 0 &&
                          !level3.list_file_path.length &&
                          level3.result != null &&
                          level3.result != 'false' &&
                          level3.result != 'true'
                        "
                      >
                        <p class="list-ok">{{ level3.result }}</p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 0 &&
                          level3.list_file_path.length &&
                          level3.result != null &&
                          level3.result != 'false' &&
                          level3.result != 'true'
                        "
                      >
                        <p class="list-ok">level3.result</p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 1 &&
                          level3.list_file_path.length &&
                          level3.result == 'false'
                        "
                      >
                        <p class="list-ok">Not ok</p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list"
                        v-if="
                          level3.require_file == 1 &&
                          level3.list_file_path.length &&
                          level3.result == 'true'
                        "
                      >
                        <p class="list-ok">Ok</p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 1 &&
                          level3.list_file_path.length &&
                          level3.result == null
                        "
                      >
                        <p class="list-ok">Not ok</p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 1 &&
                          level3.list_file_path.length &&
                          level3.result != 'true' &&
                          level3.result != 'false' &&
                          level3.result != null
                        "
                      >
                        <p class="list-ok">{{ level3.result }}</p>
                        <p>
                          <button
                            @click="viewImage(level3.list_file_path)"
                            style="
                              padding: 0px;
                              border: none;
                              background: transparent;
                              color: darkcyan;
                              text-decoration-line: underline;
                            "
                          >
                            {{ $t("VIEW_IMAGE") }}
                          </button>
                        </p>
                      </div>

                      <div
                        class="right-checklist"
                        v-if="
                          level3.require_file == 1 &&
                          !level3.list_file_path.length &&
                          level3.result == null
                        "
                      >
                        <p class="list-ok">{{ $t("NO_FILE") }}</p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 1 &&
                          !level3.list_file_path.length &&
                          level3.result == 'true'
                        "
                      >
                        <p class="list-ok">NOT Ok</p>
                        <p>
                          {{ $t("NO_FILE") }}
                        </p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 1 &&
                          !level3.list_file_path.length &&
                          level3.result == 'false'
                        "
                      >
                        <p class="list-ok">Not ok</p>
                        <p>
                          {{ $t("NO_FILE") }}
                        </p>
                      </div>

                      <div
                        class="right-checklist"
                        v-if="
                          level3.require_file == 1 &&
                          !level3.list_file_path.length &&
                          level3.result == null
                        "
                      >
                        <p class="list-ok">{{ $t("NO_FILE") }}</p>
                      </div>

                      <div
                        class="right-checklist img-check-list d-flex"
                        v-if="
                          level3.require_file == 1 &&
                          !level3.list_file_path.length &&
                          level3.result != 'true' &&
                          level3.result != 'false' &&
                          level3.result != null
                        "
                      >
                        <p class="list-ok">NOT OK</p>
                        <p>
                          {{ $t("NO_FILE") }}
                        </p>
                      </div>
                    </td>
                    <td class="text-left">{{ level3.reason }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="props.checklistrequired && props.plan.check_list_result_status == 0 && userStore.roleCheckListResult || props.checklistrequired && props.plan.check_list_result_status == null && userStore.roleCheckListResult" class="row  col-xs-12"  style="padding-top: 20px;margin: 0px auto;text-align: center;">
      <button id="channel_approved" @click="showViewApproved"  type="button" class="btn btn-primary" style="background: #58AD69;color: #fff;text-decoration: none; margin-right: 8px; border: none;">{{ $t('MARKERTING.IMAGE.APPROVED')}}</button>
      <button id="channel_reject" @click="showViewReject" type="button" class="btn btn-warning">{{$t('REJECT')}}</button>
    </div>
    
    <div v-if="props.checkListCateResult" class="row col-xs-12">
      <p style="text-align: center; color: red; padding: 10px">
        {{ $t("NO.ITEM") }}
      </p>
    </div>
  </div>
  <ApproveModal v-model="showPopupCheckListApproved" :title="$t('CONFIRM')">
    <div class="col-sm-12 d-flex">
        <p class="edit-text col-xs-3 col-md-3 col-sm-3" >{{ $t('COMMENT') }}:</p>
        <textarea class="edit-cmt col-xs-8 col-md-8 col-sm-8" style="height: 100px; padding: 4px;" v-model="commentApprove">
        </textarea>
    </div>

    <template #modalActionsApprove="modalActionsApprove">
        <button class="btn btn-primary" type="button" @click="viewChannelApproved(modalActionsApprove)">
          {{ $t("SUBMIT") }}
        </button>
    </template>
  </ApproveModal>

  <ErrorModal v-model="showPopupCheckListReject" :title="$t('CONFIRM')">
    <div class="col-sm-12 d-flex">
        <p class="edit-text col-xs-4" >{{ $t('COMMENT') }}<span style="color: red; margin-left: 4px;">*</span>:</p>
        <textarea class="edit-cmt col-xs-8 col-md-8 col-sm-8" style="height: 100px; padding: 4px;" v-model="commentReject">
        </textarea>
    </div>

    <template #modalActionsError="modalActionsError">
        <button class="btn btn-danger" type="button" @click="reject(modalActionsError)">
          {{ $t("SUBMIT") }}
        </button>
    </template>
  </ErrorModal>
  <Modal v-model="listImage" :title="$t('VIEW_IMAGE')">
      <PopupShowImage :filebase64VisitPlan="filebase64VisitPlan" />
  </Modal>
</template>
<style scoped>
.tabs {
  display: flex;
  cursor: pointer;
}

.tabs div {
  padding: 10px;
  border: 1px solid #ccc;
  margin: 2px;
}

.tabs div.active {
  background-color: #e0e0e0;
}

.tabs {
  background: #add8e6;
}

.tab-content {
  margin-top: 10px;
}
.modal-check-list .nav-tabs {
  display: flex;
}
.nav-tabs > li {
  font-size: 14px;
  font-weight: 500;
  padding: 15px;
}
.nav-tabs > li.active {
  color: #ffffff;
  background-color: #209e91;
}

.list-ok {
  min-width: 70px;
}
img-check-list p {
  margin-bottom: 0px;
}
</style>
