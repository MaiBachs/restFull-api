<script setup>
import { defineEmits, defineProps, computed, ref } from "vue";
import { notify } from "@kyvg/vue3-notification";
import axios from "axios";
import popupModal from "@/app/popupModal.vue";
import { useLoaddingStore } from "./../../store/LoaddingStore";
const loaddingStore = useLoaddingStore();
const emit = defineEmits(["onOk", "update:modelValue"]);
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  listPenaltyDetail: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});
const editItem = computed({
  get() {
    return props.modelValue || {};
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
const isSaving = ref(false);
async function confirmEditPenalty(action) {
  loaddingStore.loading = true;
  try {
    isSaving.value = true;
    const detailsData = editItem.value.detail_data.map(item => ({
      gravedad: item.gravedad,
      penalidad: item.penalidad,
    }));
    const response = await axios.put("/api/penalty/edit", {
      evaluation_type: editItem.value.evaluation_type,
      user_type: editItem.value.user_type,
      detail_data: detailsData,
    });
    isSaving.value = false;
    if (response && response.data.status && response.data.data != null) {
      notify({
        type: "success",
        title: "Edit Successful",
        text: response.data.status.message,
        duration: 10000,
      });
      loaddingStore.loading = false;
      action.onClose();
      emit("clearValue");
    } else {
      notify({
        type: "error",
        title: "Edit Error",
        text: response.data.status.message,
        duration: 10000,
      });
      loaddingStore.loading = false;
    }
  } catch (error) {
    console.error(error);
    isSaving.value = false;
    loaddingStore.loading = false;
  }
}
</script>

<template>
  <popupModal
    modal-id="exampleModalEditPenalty"
    :title="$t('EDIT_PENALTY')"
    @close-modal="closeModal"
  >
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-3 form-label">{{ $t("EVALUATION") }}:</label>
          <input
            v-model="editItem.evaluation_type"
            class="form-control-input col-md-7"
            type="text"
            readonly
          />
        </div>
      </div>
      <div class="col-md-6 text-center"></div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-3 form-label">{{ $t("USER") }}:</label>
          <input
            v-model="editItem.user_type"
            class="form-control-input col-md-7"
            type="text"
            readonly
          />
        </div>
      </div>
      <div class="col-md-6 text-center"></div>
    </div>
    <fieldset>
      <legend>{{ $t("DETAIL") }}</legend>
      <div v-for="item in editItem.detail_data" :key="item">
        <div class="row col-md-12 p-b-15">
          <div class="col-md-3 text-center">
            <div class="row">
              <div class="col-md-6">
                <p >{{ item.gravedad }}</p>
              </div>
              <div class="col-md-6">
                <input
                  v-model="item.penalidad"
                  class="form-control-input col-md-7"
                  type="text"
                  style="text-align: center;"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </fieldset>
    <template #modalActions="modalActionProps">
      <button
        type="submit"
        class="btn btn-primary ng-binding"
        style="width: 175px; text-align: center"
        @click="confirmEditPenalty(modalActionProps)">
        {{ $t("CONFIRM") }}
      </button>
    </template>
  </popupModal>
</template>
<style scoped>
.form-label {
  text-align: start;
}
.form-control-input {
  height: 30px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
}
.form-control-date {
  width: 58%;
  padding: 0px;
  height: 35px;
}
</style>
