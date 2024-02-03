<script setup>
import { computed, defineEmits, defineProps, onMounted, ref } from "vue";

const emit = defineEmits(["closeModal"]);
const modalLabel = computed(() => `${props.modalId}Label`);
const closeElement = ref(null);
const myModalEl = ref(null);
const { addAssignQueue, assignQueue } = useAssign(ref);
import useAssign from "@/composables/useCheckListAssign";
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => {
      [];
    },
  },
  listItemCommonEdit: {
    type: Object,
    required: true,
    default: () => {
      [];
    },
  },
  modalId: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
});
assignQueue.value = [];
function close() {
  if (closeElement.value) {
    
    closeElement.value.click()
  }
}

onMounted(() => {
  myModalEl.value.addEventListener("hide.bs.modal", (event) => {
    emit("closeModal", event);
  });
});

async function updateRowData(item) {
  addAssignQueue({ ...item });
  emit("update:modelValue", assignQueue.value);
}

function closeModal() {
  assignQueue.value = [];

}
</script>
<template>
  <div
    :id="modalId"
    ref="myModalEl"
    class="modal fade bd-example-modal-lg"
    tabindex="-1"
    role="dialog"
    :aria-labelledby="modalLabel"
    aria-hidden="true"
    data-backdrop="static"
    @close-modal="closeModal"
  >
    <div class="modal-dialog modal-xl" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 :id="modalLabel" class="modal-title">{{ title }}</h5>
          <button
            class="btn-close"
            type="button"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="" v-if="props.listItemCommonEdit?.length">
            <table class="table table-bordered" id="myTable">
              <thead class="table-success">
                <tr>
                  <th scope="col" rowspan="2">{{ $t("MENU/SUBMENU") }}</th>
                  <th scope="col">{{ $t("REQUIRE") }}</th>
                  <th scope="col">{{ $t("FILE_REQUIRE") }}</th>
                  <th scope="col">{{ $t("FILE_TYPE") }}</th>
                  <th scope="col">Index</th>
                </tr>
              </thead>
              <tbody v-for="data in props.listItemCommonEdit" :key="data">
                <tr>
                  <td class="list_parent1">{{ data.check_list_parent1 }}</td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
                <tr>
                  <td class="list_parent2">{{ data.check_list_parent2 }}</td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
                <tr
                  class="editable-row"
                  v-for="item in data.check_list_parent3s"
                  :key="item"
                >
                  <td>{{ item.job_name }}</td>
                  <td>
                    <input
                      type="checkbox"
                      v-model="item.required"
                      @change="updateRowData(item)"
                    />
                  </td>
                  <td>
                    <input
                      type="checkbox"
                      v-model="item.required_file"
                      @change="updateRowData(item)"
                    />
                  </td>
                  <td>
                    <p v-if="item.result_data_type == 1">Photo</p>
                    <p v-if="item.result_data_type == 0"></p>
                  </td>
                  <td class="index-input">
                    <input
                      @change="updateRowData(item)"
                      v-model="item.idx"
                      type="text"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="modal-footer">
          <slot name="modalActions" :onClose="close"></slot>
          <button
            class="btn btn-primary"
            ref="closeElement"
            type="button"
            data-bs-dismiss="modal"
          >
            {{ $t("CLOSE") }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
