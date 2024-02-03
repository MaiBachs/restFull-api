<script setup>
import addAuditors from './addAuditors.vue';
import manageAuditors from './manageAuditors.vue';
import { getBranch } from '../../store/getBranch'
import { getChannelType } from '../../store/getChannelType'
import { useAuditEvalutionStore } from '../../store/AuditEvalutionStore'
import { getShopChannelAudit } from '../../store/getShopChannel'

const auditEvaluation = useAuditEvalutionStore();
auditEvaluation.getAuditor();
const shopChannelAudit = getShopChannelAudit();
shopChannelAudit.actionGetShopChannel();
const listBranch = getBranch();
listBranch.actionGetBranch();
const listChannelType = getChannelType();
listChannelType.actionGetChannelType();

</script>
<template>
   <div class="widgets">
      <div class="row">
         <div class="col-sm-12">
            <div class="card">
            <div class="card-header pb-0">
               <h5>{{ $t('AUDITS_EVALUATION') }}</h5>
            </div>
            <div class="card-body">
            <div class="tabbed-card">
               <ul class="nav nav-tabs nav-primary" id="pills-clrtab1" role="tablist">
                  <li class="nav-item nav-primary">
                     <a class="nav-link active" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-clrhome" role="tab" aria-controls="pills-clrhome" aria-selected="true"><i class="icofont icofont-ui-home"></i>{{ $t('AUDITS_EVALUATION') }}</a>
                  </li>
                  <li class="nav-item">
                     <a class="nav-link" id="pills-clrprofile-tab" data-bs-toggle="pill" href="#pills-clrprofile" role="tab" aria-controls="pills-clrprofile" aria-selected="false"><i class="icofont icofont-man-in-glasses"></i>{{ $t('ADD_AUDITTORS') }}</a>
                  </li>
               </ul>
               <div class="tab-content" id="pills-clrtabContent1">
                  <div class="p-t-20 tab-pane fade show active" id="pills-clrhome" role="tabpanel" aria-labelledby="pills-clrhome-tab" >
                     <manageAuditors />
                  </div>
                  <div class="p-t-20 tab-pane fade" id="pills-clrprofile" role="tabpanel" aria-labelledby="pills-clrprofile-tab">
                     <addAuditors />
                  </div>
               </div>
            </div>
            </div>
            </div>
         </div>
      </div>
   </div>
</template>
  
  