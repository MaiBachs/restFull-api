<script setup>
  import Multiselect from "@vueform/multiselect";
  import popupModal from "@/components/common/popupModalCommon.vue";
  import useChannelManager from "@/composables/useChannelManager"
  import Modal from '@/components/common/customModal.vue'
  import tableListMapPlanChannel from "@/components/channelManagerBranch/widgets/tableListMapPlanChannel.vue"

  import { defineProps,ref } from "vue";
  const props = defineProps({
    searchData: {
      type: Object,
      required: true,
      default: () => ({}),
    },
    listMapPlan: {
      type: Object,
      required: true,
      default: () => ({}),
    },
  });
  const  {getListMapPlan, listMapPlanChannelDevelops} = useChannelManager(ref);
    const listmonth = [
        {
            name:'01',
            value:'01'
            
        },
        {
            name:'02',
            value:'02'
        },
        {
            name:'03',
            value:'03'
        },
        {
            name:'04',
            value:'04'
        },
        {
            name:'05',
            value:'05'
        },
        {
            name:'06',
            value:'06'
        },
        {
            name:'07',
            value:'07'
        },
        {
            name:'08',
            value:'08'
        },
        {
            name:'09',
            value:'09'
        },
        {
            name:'10',
            value:'10'
        },
        {
            name:'11',
            value:'11'
        },
        {
            name:'12',
            value:'12'
        }
    ]
    const detailFromDate = ref("")
    detailFromDate.value = listmonth[0].value
    const showDataListMapChannel = ref(false)
    async function actionListMapPlan() {
      console.log(detailFromDate.value)
      await getListMapPlan(props.listMapPlan,detailFromDate.value,props.searchData.plan_date)
      showDataListMapChannel.value = true
      console.log(showDataListMapChannel.value)

    }
  </script>

  <template>
    <popupModal modal-id="actionListMapPlan" :title="$t('CHANNEL_MANAGEMENT')" @close-modal="closeModal">
        <div class="row col-xs-12" style="padding: 0px 19px 19px; color: black; font-weight: 500;">
          {{ $t("SELECT_MONTH_VIEW_DETAIL") }}
        </div>
        <div v-if="props.listMapPlan.staff_code" class="row col-xs-12" style="padding: 0px 19px 15px 6px; color: black;">
          <div class="col-md-2 p-0">{{$t("HOME.SEARCH.COMBOBOX.ZONAL") }}:</div>
            <div class="col-md-6"><strong style="font-weight: 500;
              margin-left: 7px;">{{props.listMapPlan.staff_code}}</strong></div>
        </div>
        <div class="row col-xs-12" style="display: flex; align-items: center;">         
          <div class="col-xs-2 col-md-2 col-sm-2">{{ $t("MONTH") }}:</div>
            <div class="form-select-label col-xs-6 col-md-6 col-sm-6" style="display: flex; align-items: center;">
                <Multiselect
                  v-model="detailFromDate"
                  :options="listmonth"
                  track-by="name"
                  label="name"
                  :canClear="false"
              ></Multiselect>
            </div>

        </div>
        <div class="row d-flex p-t-15">         
          <div class="col-md-2"><label>{{$t("YEAR") }}:</label></div>
          <div class="col-md-6">
              <strong style="font-weight: 400;margin-left: 7px;">{{ props.searchData.plan_date}}</strong>
          </div>
            
        </div>

        <template #modalActions="modalActionProps">
          <button v-if="detailFromDate" class="btn btn-primary" type="button" @click="actionListMapPlan(modalActionProps)">
            {{ $t("SUBMIT") }}
          </button>
        </template>

        <Modal v-model="showDataListMapChannel" :title="$t('CHANNEL_MANAGEMENT')">
          <tableListMapPlanChannel :listMapPlanChannelDevelops="listMapPlanChannelDevelops" :listMapPlan="props.listMapPlan"/>
        </Modal>
    </popupModal>
    

  </template>