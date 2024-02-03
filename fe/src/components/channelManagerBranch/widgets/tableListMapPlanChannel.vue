<script setup>
import {defineProps, ref} from "vue";
import useChannelManager from "@/composables/useChannelManager"
import popupCommon from "@/components/common/popupCommon.vue";
const props = defineProps({
    listMapPlanChannelDevelops: {
      type: Object,
      required: true,
      default: () => ({}),
    },
    listMapPlan : {
      type: Object,
      required: true,
      default: () => ({}),
    },
  });
  const  {statusDecodeBc, approvedMapPlanChannel} = useChannelManager(ref);
  const showApproved = ref(false)
  const commentApproved = ref("")
  const staffCode = ref("")
  const itemId = ref("")
  const brCode = ref("")
  function clickApproved(id,staff_code,br_code) {
    itemId.value = id
    showApproved.value = true
    staffCode.value = staff_code
    brCode.value = br_code
  }
  async function approved() {
    await approvedMapPlanChannel(itemId.value,props.listMapPlan,commentApproved.value,staffCode.value,brCode.value)

  }
</script>
<template>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="table-success">
                <tr class="black-muted-bg">
                    <th>#</th>
                    <th>{{ $t("HOME.SEARCH.COMBOBOX.ZONAL")}}</th>
                    <th>{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE")}}</th>
                    <th>{{ $t("HOME.SEARCH.BTS.AMOUNT")}}</th>
                    <th>{{ $t("lAT")}}</th>
                    <th>{{ $t("LONG")}}</th>
                    <th>{{ $t("Mapa") }}</th>
                    <th>{{ $t("CHANNEL_CODE")}}</th>
                    <th>{{ $t("VISIT_PLAN.TABLE.LABEL.PLANDATE")}}</th>
                    <th>{{ $t("FROM_DATE")}}</th>
                    <th>{{ $t("TO_DATE")}}</th>
                    <th>{{ $t("COMMENT")}}</th>
                    <th>{{ $t("CHECK_LIST")}}</th>
                    <th>{{ $t("HOME.SEARCH.MAP.ACTION")}}</th>
                    
                </tr>
            </thead>
            <tbody>
                <tr v-for="(sale ,index) in props.listMapPlanChannelDevelops" :key="sale" class="no-top-border">
                    <td>{{index + 1}}</td>
                    <td style="text-align: left;">{{sale.staff_code}}</td>
                    <td class="text-channel-code">{{sale.channel_type_name}}</td>
                    <td>{{sale.amount}}</td>
                    <td>{{sale.lats}}</td>
                    <td>{{sale.longs}}</td>
                    <td>
                        <span v-if="sale.channel_type_id != 10" @click="viewLocationChannel(sale.channel_type_id,sale.lats,sale.longs,sale.id)"
                            style="
                                color: darkcyan;
                                text-decoration-line: underline;
                                cursor: pointer;
                                font-size: 13px;
                            "
                            >
                            {{ $t("VIEW_ON_MAP") }}</span
                            >
                    </td>
                    <td>{{sale.channel_code_deployed}}</td>
                    <td class="text-channel-code">{{sale.created_date}}</td>
                    <td>{{sale.from_date}}</td>
                    <td>{{sale.to_date}}</td>
                    <td>{{sale.channel_comment}}</td>
                    <td >
                        <button v-if="sale.channel_type_id != 80043 || sale.channel_type_id != 10" @click="viewChecklist(sale.id)" style=" border: none; background: transparent;color: darkcyan; text-decoration-line: underline;">{{$t("VIEW_CHECKLIST") }}</button>
                    </td>
                    <td>
                        <div class="d-flex">
                            <button v-if="sale.status == 0" class="btn btn-primary btn-xs" @click="clickApproved(sale.id, sale.staff_code, sale.br_code)">{{$t("MARKERTING.IMAGE.APPROVED")}}</button>
                            <button v-if="sale.status == 0" class="btn btn-danger btn-xs" @click="clickReject(sale.id)">{{$t("REJECT")}}</button>
                        </div>
                        
                        <span v-if="sale.status != 0 " :style="sale.status == 1 && {'color': '#4CAF50'} || sale.status == -1 && {'color': 'red'}">{{ statusDecodeBc(sale.status) }}</span>
                    </td>
                    
                </tr>
            </tbody>
        </table>
    </div>

    <popupCommon v-model="showApproved" :title="$t('CHANNEL_MANAGEMENT')">
        <div class="row">
            <p class="col-md-3" >{{$t('COMMENT') }}:</p>
            <textarea class="col-md-8" style="height: 100px; padding: 4px;" v-model="commentApproved">
            </textarea>
        </div>
        <template #modalActionsCommon="modalActionsCommon">
        <button class="btn bg-primary" type="button" @click="approved(modalActionsCommon)">
          {{ $t("APROBAR") }}
        </button>
    </template>
    </popupCommon>
</template>
<style scoped>
td {
    padding: 5px;
}
</style>