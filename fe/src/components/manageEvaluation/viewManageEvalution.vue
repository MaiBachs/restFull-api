<script setup>
import manageEvalution from "@/components/manageEvaluation/manageEvalution.vue";
import { getChannelTypeChecklist} from "@/store/getChannelTypeChecklist";
import { getParent } from "@/store/getParent";
import { useAuditEvalutionStore } from '../../store/AuditEvalutionStore'
const getListParent = getParent();
getListParent.actionGetParent();
const getListChannelTypeChecklist = getChannelTypeChecklist();
getListChannelTypeChecklist.actionGetChannelTypeChecklist();
const auditEvaluation = useAuditEvalutionStore();
auditEvaluation.getAuditor();
</script>
<template>
  <div class="widgets">
    <div class="row">
      <div class="col-sm-12">
        <div class="card">
          <div class="card-header pb-0">
            <h5>{{ $t('MANAGEMENT_EVALUTION') }}</h5>
          </div>
          <div class="card-body">
            <div class="tabbed-card">
              <ul class="nav nav-tabs nav-primary" id="pills-clrtab1" role="tablist">
                <li class="nav-item nav-primary">
                  <a class="nav-link active" id="pills-clrhome-tab" data-bs-toggle="pill" href="#pills-clrhome" role="tab" aria-controls="pills-clrhome" aria-selected="true"><i class="icofont icofont-ui-home"></i>{{ $t('MANAGEMENT_EVALUTION') }}</a>
                </li>
              </ul>
              <div class="tab-content" id="pills-clrtabContent1">
                <div class="p-t-20 tab-pane fade show active" id="pills-clrhome" role="tabpanel" aria-labelledby="pills-clrhome-tab" >
                  <manageEvalution />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>