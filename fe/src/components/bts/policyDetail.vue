<script setup>
import {defineProps, computed} from "vue"
import {defineEmits} from "vue"
import popupModal from "@/app/popupModal.vue";

const emit = defineEmits(['onOk', 'update:modelValue'])
const props = defineProps({
  modelValue: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  options: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  listPolicy: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  ListSaleTime: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});


const itemDetail = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value)
  }
},);
const formattedStartDate = computed({
  get: () => formatDate(itemDetail.value.start_date),
});
const formattedEndDate = computed({
  get: () => formatDate(itemDetail.value.end_date),
});
const formatDate = (date) => {
  if (!date) return '';
  const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
  return new Date(date).toLocaleDateString('en-GB', options);
};
const formattedStatus = computed(() => {
  if (itemDetail.value.status === 1) {
    return "Active";
  } else if (itemDetail.value.status === 0) {
    return "Inactive";
  }
  return "Unknown";
});

</script>
<template>
  <popupModal modal-id="exampleModalDetailPolicy" :title="$t('DETAIL_POLICY')">
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("POLICY") }}:</label>
          <input
              v-model="itemDetail.name"
              class="form-control-input col-md-7"
              type="text"
              readonly
              style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end;margin: 0 auto;min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;padding-left: 26px;font-size: medium;font-weight: 500;"
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("DESCRIPTION") }}:</label>
          <input
              v-model="itemDetail.content"
              class="form-control-input col-md-7"
              type="text"
              readonly
              style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end;margin: 0 auto;min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;padding-left: 26px;font-size: medium;font-weight: 500;"
          />
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("STATUS") }}:</label>
          <input
              v-model="formattedStatus"
              class="form-control-input col-md-7"
              type="text"
              readonly
              style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end;margin: 0 auto;min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;padding-left: 26px;font-size: medium;font-weight: 500;"
          />
        </div>
      </div>
      <div class="col-md-6 text-center">
        <div class="row">
          <label class="col-md-5 form-label">{{ $t("FROM_DATE") }}:</label>
          <input
            v-model="formattedStartDate"
            class="form-control-input col-md-7"
            type="text"
            readonly
            style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end;margin: 0 auto;min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;padding-left: 26px;font-size: medium;font-weight: 500;"
/>
        </div>
      </div>
    </div>
    <div class="row col-md-12 p-b-15">
        <div class="col-md-6 text-center">
            <div class="row">
                <label class="col-md-5 form-label">{{ $t("TO_DATE") }}:</label>
                <input
                    v-model="formattedEndDate"
                    class="form-control-input col-md-7"
                    type="text"
                    readonly
                    style="align-items: center;box-sizing: border-box;cursor: pointer;display: flex;justify-content: flex-end;margin: 0 auto;min-height: calc(var(--ms-border-width, 1px)*2 + var(--ms-font-size, 1rem)*var(--ms-line-height, 1.375) + var(--ms-py, .5rem)*2);outline: none;position: relative;width: 300px;padding-left: 26px;font-size: medium;font-weight: 500;"
                />
            </div>
        </div>
        <div class="col-md-6 text-center"></div>
    </div>
      <div class="row col-md-12 p-b-15">
        <div class="col-md-6 text-center">
          <div class="row">
          <label class="col-md-5 form-label">{{ $t("SALE_TIME") }}:</label>
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
            <tr>
              <th scope="col">{{ $t("TIME_FROM") }}</th>
              <th scope="col">{{ $t("TIME_TO") }}</th>
            </tr>
            </thead>
            <tbody v-for="item in ListSaleTime" :key="item">
            <tr>
              <td>{{ item.time_from}}</td>
              <td>{{ item.time_to }}</td>
            </tr>
            </tbody>
          </table>
        </div>
        </div>
      </div>
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
