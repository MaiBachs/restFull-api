<script setup>
import axios from "axios"
import { i18n } from '../../main.js';
import { useLoaddingStore } from "../../store/LoaddingStore";
import popupModal from '../../app/popupModal.vue'
const loaddingStore = useLoaddingStore();
import { notify } from "@kyvg/vue3-notification";
import { ref, defineProps, defineEmits} from "vue"
import usePlanAssignment from "@/composables/useMenuTreePlanAssignment"
import useAssign from "@/composables/useCheckListAssign"
const {changeInputLevel ,expandsLevel,changeInputLevelMenu2, expandsLevel2} = usePlanAssignment(ref)
const emit = defineEmits(['update:modelValue'])
const {deleteAssignQueue , assignQueue, deleteAssignQueueLevel2, deleteAssignQueueLevel1} = useAssign(ref)
const listPlanJobId = ref([]);
const planJoParentId1 = ref(false)
const planJoParentId2 = ref(false)
const props = defineProps({
  listItemDelete: {
    type: Object,
    required: true,
    default: () => {[]}
  },
  listItemCommonDelete: {
    type: Object,
    required: true,
    default: () => {[]}
  },
});

function changeInputLevel1(id) {
  changeInputLevel(id)
}
function changeInputLevel2(id) {
  changeInputLevelMenu2(id)
}

function getListDelete(item) {
  if(item.valueDelete === true) {
    item.valueDelete = false
    assignQueue.value = []
  }else {
    item.valueDelete = true
  }
  if(item.valueDelete == true) {
    deleteAssignQueue({...item})
  }
}
async function deleteItemAssign (action)  {
  loaddingStore.loading = true;
  if(assignQueue.value && assignQueue.value.length) {
    listPlanJobId.value = assignQueue.value.filter(o => (o.valueDelete == true))
  }
  if(listPlanJobId.value && listPlanJobId.value.length) {
    listPlanJobId.value = listPlanJobId.value.map(function (o) {
      if(o) {
        return {
          plan_job_id: o.plan_job_id || o.plan_job_parent_id2 || o.plan_job_parent_id1 ,
        }
      }
      
    })
    if(listPlanJobId.value && listPlanJobId.value.length) {
        try {
        const response = await axios.put("/api/plan-job/delete-assign-checklist", listPlanJobId.value);
          if(response.status == 200) {
            notify({
              type: "success",
              text: i18n.global.t('SUCCESS_DELETE'),
              duration: 30000,
            });
            listPlanJobId.value = [];
            action.onClose()
            emit('clearValue')
            
          }
        } catch (error) {
          loaddingStore.loading = false;
          notify({
            type: "error",
            title: "Get current user",
            text: error,
            duration: 30000,
          });
        }
    }else {
      notify({
        type: "success",
        text: i18n.global.t('SUCCESS_DELETE'),
        duration: 30000,
      });
    }
    }
}

function getListDelete1(parent,data) {
  if(planJoParentId1.value == true) {
    planJoParentId1.value = false
    parent.valueDelete = false
    assignQueue.value = []
    for (let i = 0; i < data.length; i++) {
      data[i].valueDelete = false
      if(data[i].check_list_parent3s) {
         let level3 = data[i].check_list_parent3s
          for(let o = 0; o < level3.length; o++) {
            level3[o].valueDelete = false
          }
        }
    }  
  } else {
    planJoParentId1.value = true
    parent.valueDelete = true
    for (let i = 0; i < data.length; i++) {
      data[i].valueDelete = true
      data[i].check_list_parent3s = data[i].check_list_parent3s.filter(item => item.valueDelete = true);
    }
  }
  if(planJoParentId1.value === true) {
    deleteAssignQueueLevel1({...parent})
    for (let i = 0; i < data.length; i++) {
      deleteAssignQueueLevel2({...data[i]})
      if(data[i].check_list_parent3s) {
         let level3 = data[i].check_list_parent3s
          for(let o = 0; o < level3.length; o++) {
            deleteAssignQueue({...level3[o]})
          }
        }
    }
  }
  
}
function getListDelete2(parent) {
  if(parent.valueDelete == true) {
    parent.valueDelete = false;
    assignQueue.value = [];
    if(parent.check_list_parent3s) {
      let level3 = parent.check_list_parent3s
      for(let o = 0; o < level3.length; o++) {
        level3[o].valueDelete = false
      }
    }
  } else {
    parent.valueDelete = true
    parent.check_list_parent3s = parent.check_list_parent3s.filter(item => item.valueDelete = true);
  }
  if(parent.valueDelete == true) {
    deleteAssignQueueLevel2({...parent})
    for(let o = 0; o < parent.check_list_parent3s.length; o++) {
      deleteAssignQueue({...parent.check_list_parent3s[o]})
    }
  }
}
function closeModal() {
  planJoParentId1.value = false;
  planJoParentId2.value = false;
  listPlanJobId.value = []
  assignQueue.value = []
}
</script>
<template>
  <popupModal modal-id="popupShowDeleteAssign" title="Delete Assign" @close-modal="closeModal">
    <div  v-if="props.listItemDelete?.length">
        <table class="table table-bordered table-hover" id="myTable">
          <thead class="table-success">
            <tr>
              <th scope="col" style="width: 40%;">{{ $t("MENU/SUBMENU") }}</th>
              <th scope="col">{{ $t("REQUIRE") }}</th>
              <th scope="col">{{ $t("FILE_REQUIRE") }}</th>
              <th scope="col">{{ $t("FILE_TYPE") }}</th>
              <th scope="col">Index</th>
              <th scope="col">{{ $t("SELECT_DELETE") }}</th>
            </tr>
          </thead>
          <tbody v-for="data in props.listItemDelete" :key="data">
            <tr>
              <td class="list_parent1">
                <div class="" @click="changeInputLevel1(data.check_list_parent_id1)">
                  <i v-if="expandsLevel.includes(data.check_list_parent_id1)" class="fa fa-plus-square-o"></i>
                  <i v-if="!expandsLevel.includes(data.check_list_parent_id1)" class="fa fa-minus-square-o"></i>
                  {{ data.check_list_parent1 }}
                </div>
              </td>
              <td></td>
              <td></td>
              <td></td>
              <td class="check-icon">{{ data.idx1 }}</td>
              <td style="text-align: center;">
                <input type="checkbox"  @click="getListDelete1(data,data.search_assign_check_list_response_lv2)" v-model="planJoParentId1"/> 
               
              </td>
            </tr>
            <template
                v-for="lv2 in data.search_assign_check_list_response_lv2"
                :key="lv2.check_list_parent_id2"
              >
                <tr v-if="!expandsLevel.includes(data.check_list_parent_id1)">
                  <td class="list_parent2">
                    <div class="" @click="changeInputLevel2(lv2.check_list_parent_id2)">
                      <i v-if="expandsLevel2.includes(lv2.check_list_parent_id2)" class="fa fa-plus-square-o"></i>
                      <i v-if="!expandsLevel2.includes(lv2.check_list_parent_id2)" class="fa fa-minus-square-o"></i>
                      {{ lv2.check_list_parent2 }}
                    </div>
                  </td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td class="check-icon">{{ lv2.idx2 }}</td>
                  <td style="text-align: center;">
                    <input type="checkbox" :disabled="planJoParentId1"  @click="getListDelete2(lv2)" v-model="lv2.valueDelete"/> 
                  </td>
                </tr>
                <template v-if="!expandsLevel.includes(data.check_list_parent_id1) && !expandsLevel2.includes(lv2.check_list_parent_id2)">
                  <tr
                    v-for="lv3 in lv2?.check_list_parent3s || []"
                    :key="lv3"
                  >
                  <td class="list_parent3">{{ lv3.job_name }}</td>
                  <td class="check-icon">
                    <i v-if="lv3.required == 1" class="fa fa-check-square-o"></i>
                    <p v-if="lv3.required == 0"></p>
                  </td>
                  <td class="check-icon">
                    <i
                      v-if="lv3.required_file == 1"
                      class="fa fa-check-square-o"
                    ></i>
                    <p v-if="lv3.required_file == 0"></p>
                  </td>
                  <td class="check-icon">
                    <p v-if="lv3.file_type == 1">Photo</p>
                    <p v-if="lv3.file_type != 1"></p>
                  </td>
                  <td class="check-icon">{{ lv3.idx }}</td>
                  <td style="text-align: center;">
                    <!-- @click="getListDelete(data.check_list_parent3s,index,lv3.plan_job_id)" -->
                    <input type="checkbox" :disabled="planJoParentId1 || lv2.valueDelete" @click="getListDelete(lv3)" v-model="lv3.valueDelete"/>
                  
                  </td>
                  </tr>
                </template>
                
            </template>






          </tbody>
        </table>
    </div>
    <template #modalActions="modalActionProps">
      <button v-if="assignQueue.length" class="btn btn-danger" type="button" @click="deleteItemAssign(modalActionProps)">
        {{ $t('DELETE') }}
      </button>
    </template>
  </popupModal>
</template>
<style scoped>
.index-input input {
    border: 1px solid #cdcdcd;
    border-radius: 5px;

}
.require {
  color: red;

}
.list_parent1 {
  font-weight: 700;
}
.list_parent2 {
  font-weight: 500;
  padding-left: 20px;
}
.list_parent3 {
  padding-left: 40px;
}
.row-edit {
  display: flex;
}
.row-edit input {
  margin-right: 5px;

}
  input[type="checkbox"] {
    accent-color: #24695c;
  }
</style>
