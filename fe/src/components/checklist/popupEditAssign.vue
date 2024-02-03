<script setup>
import axios from "axios"
import { i18n } from '../../main.js';
import { useLoaddingStore } from "../../store/LoaddingStore";
import popupModal from "@/app/popupModal.vue";
import useAssign from "@/composables/useCheckListAssign"
import { notify } from "@kyvg/vue3-notification";
const loaddingStore = useLoaddingStore();
import { ref ,defineProps , defineEmits ,reactive ,computed} from "vue"
const emit = defineEmits(['onOk', 'update:modelValue'])
import usePlanAssignment from "@/composables/useMenuTreePlanAssignment"
const {changeInputLevel ,expandsLevel,changeInputLevelMenu2, expandsLevel2} = usePlanAssignment(ref)
const {addAssignQueue , editAssignQueue, assignQueue, valueEditAssignQueue, editValueAssignQueue} = useAssign(ref)
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => {[]}
  },
  compareListItemCommonEdit: {
    type: Object,
    required: true,
    default: () => {[]}
  }
});

const editItem = computed({
  get() {
    return props.modelValue || {}
  },
  set(value) {
    emit('update:modelValue', value)
  }
})


const canOk = computed(() => JSON.stringify(props.compareListItemCommonEdit) !== JSON.stringify(editItem.value))
const itemEditAssignLevel1 = reactive({
  job_id: "",
  idx: null,
  plan_id: "",
});
const itemEditAssignLevel2 = reactive({
  job_id: "",
  idx: null,
  plan_id: "",
});

async function updateRowData (item)  {
  addAssignQueue({...item})
  if (!/^[0-9]\d*$/.test(item.idx)) {
    item.idx = ""
      notify({
        type: "error",
        title: "Get current user",
        text: i18n.global.t('PLEASE_ENTER_POSITIVE'),
        duration: 10000,
      });
      return;
    }
  if(!item && item.idx != 0 &&  item.idx == "") {
    notify({
      type: "error",
      text: i18n.global.t('PLEASE_ENTER_NUMBER_ONLY'),
      duration: 3000,
    });
    return ;
  }

}
function changeInputLevel1(id) {
  changeInputLevel(id)
}
function changeInputLevel2(id) {
  changeInputLevelMenu2(id)
}

function updateRowDataLevel1(item) {
  itemEditAssignLevel1.job_id = item.check_list_parent_id1
  itemEditAssignLevel1.idx = item.idx1
  itemEditAssignLevel1.plan_id = item.plan_id
  editAssignQueue({...itemEditAssignLevel1})
  if(!item.idx1 && item.idx1 != 0) {
    notify({
      type: "error",
      text: i18n.global.t('REQUIRED_LEVEL_1_2'),
      duration: 30000,
    });
    return ; 
  }
  else if (!/^[0-9]\d*$/.test(item.idx1)) {
    item.idx1 = null
      notify({
        type: "error",
        title: "Get current user",
        text: i18n.global.t('PLEASE_ENTER_POSITIVE'),
        duration: 10000,
      });
      return;
    }

}
function updateRowDataLevel2(item) {
  itemEditAssignLevel2.job_id = item.check_list_parent_id2
  itemEditAssignLevel2.idx = item.idx2
  itemEditAssignLevel2.plan_id = item.plan_id
  editAssignQueue({...itemEditAssignLevel2})
  if(!item.idx2 && item.idx2 != 0) {
    notify({
      type: "error",
      text: i18n.global.t('REQUIRED_LEVEL_1_2'),
      duration: 30000,
    });
    return ;
    
  }
  else if (!/^[0-9]\d*$/.test(item.idx2)) {
    item.idx2 = ""
      notify({
        type: "error",
        title: "Get current user",
        text: i18n.global.t('PLEASE_ENTER_POSITIVE'),
        duration: 10000,
      });
      return;
    }
  
}
function closeModal() {
  assignQueue.value = []
  editValueAssignQueue.value = []
}
const updateEditAssign  = async (action) =>  {
  let value = []
  editValueAssignQueue.value = []
  let shouldContinueProcessing = true;
  assignQueue.value.forEach(function (o) {
  if (o.idx !== 0 && o.idx == "" || !/^[0-9]\d*$/.test(o.idx)) {
    return shouldContinueProcessing = false;
  }
  value = {
    job_id: o.job_id,
    plan_id: o.plan_id,
    required: o.required === true ? 1 : 0,
    required_file: o.required_file === true ? 1 : 0,
    idx: o.idx,
  };

  valueEditAssignQueue({...value})
});
  if (shouldContinueProcessing) {
    try {
      loaddingStore.loading = true;
      const response = await axios.put("/api/plan-job/edit-assign-checklist", editValueAssignQueue.value);
      action.onClose()
      assignQueue.value = []
      emit('clearValue')
      if(response.status == 200) {
        notify({
          type: "success",
          text: i18n.global.t('EDIT_SUCCESSS'),
          duration: 30000,
        });
        
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
  } else {
    notify({
        type: "error",
        text: i18n.global.t('NOT_INDEX'),
        duration: 30000,
      });
      return
  }
}

</script>
<template>
  <popupModal modal-id="popupShowEditAssign" title="Edit Assign" @close-modal="closeModal">

    <div v-if="editItem?.length">
        <table class="table table-bordered table-hover">
          <thead class="table-success">
            <tr>
              <th scope="col" rowspan="2">{{ $t("MENU/SUBMENU") }}</th>
              <th scope="col">{{ $t("REQUIRE") }}</th>
              <th scope="col">{{ $t("FILE_REQUIRE") }}</th>
              <th scope="col">{{ $t("FILE_TYPE") }}</th>
              <th scope="col">Index<span class="lable-require">*</span></th>
            </tr>
          </thead>
          <tbody v-for="data in editItem" :key="data">
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
              <td class="index-input">
                <input @change="updateRowDataLevel1(data,data.idx1)" v-model="data.idx1" type="number">
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
                  <td class="index-input">
                    <input @change="updateRowDataLevel2(lv2,lv2.idx2)" v-model="lv2.idx2" type="number">
                  </td>
                </tr>
                <template v-if="!expandsLevel.includes(data.check_list_parent_id1) && !expandsLevel2.includes(lv2.check_list_parent_id2)">
                  <tr
                    v-for="lv3 in lv2?.check_list_parent3s || []"
                    :key="lv3"
                  >
                    <td class="list_parent3">{{ lv3.job_name }}</td>
                    <td><input type="checkbox" v-model="lv3.required" @change="updateRowData(lv3)"></td>
                    <td><input type="checkbox" v-model="lv3.required_file" @change="updateRowData(lv3)"></td>
                    <td>
                      <p v-if="lv3.file_type == 1">Photo</p>
                      <p v-if="lv3.file_type != 1"></p>
                    </td>
                    <td class="index-input">
                      <input @change="updateRowData(lv3)" v-model="lv3.idx" type="number">
                    </td>
                  </tr>
                </template>
                
            </template>
          </tbody>
        </table>
    </div>


    <template #modalActions="modalActionProps">
      <button v-if="canOk" class="btn btn-primary" type="button" @click="updateEditAssign(modalActionProps)">
        {{ $t("SUBMIT") }}
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
</style>
