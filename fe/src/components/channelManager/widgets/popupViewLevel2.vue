<script setup>
import { defineProps,} from "vue";
import TableChannelBranch from "@/components/channelManagerBranch/widgets/tableChannelBrach.vue"

const props = defineProps({
   listChannelLevel2: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  statusApprove: {
    type: Object,
    required: true,
    default: () => ({}),
  },
  
});
</script>
<template>
    <div class="row col-xs-12">
        <TableChannelBranch :listChannelLevel2="props.listChannelLevel2"/>
        <div ng-if="props.listSearchChannel.length">
          <button v-if="props.statusApprove == 3" ng-click="showViewChannelApproved"  type="button" class="btn btn-primary" style="background: #58AD69;color: #fff;text-decoration: none; margin-right: 8px; border: none;">{{ $t('MARKERTING.IMAGE.APPROVED')}}</button>
          <button v-if="props.statusApprove == 3" ng-click="showViewChannelReject" type="button" style="background: #FF7F50 ;color: #fff;text-decoration: none; border: none" class="btn btn-primary">{{ $t('REJECT')}}</button>
       </div>
       <div v-if="props.listChannelLevel2.length < 1" class="row col-xs-12">
          <p style="text-align: center;color: red; padding: 10px;">{{ $t('NO.ITEM') }}</p>
       </div>

    </div>
    <Modal v-model="showPopupViewLevel3" :title="$t('CHANNEL_MANAGEMENT')">
      <popupViewLevel3 :listChannelLevel3= "listChannelLevel3"/>
  </Modal>
</template>