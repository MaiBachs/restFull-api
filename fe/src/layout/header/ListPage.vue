<script setup>
import { ref } from "vue";
import { useUserStore } from "./../../store/UserStore";
const isOpenBTS = ref(false);
const isOpenEvaluationofChannels = ref(false);
const isOpenVisitPlanManagement = ref(false);
const showBTSMenu = () => {
  isOpenBTS.value = true;
};
const hideBTSMenu = () => {
  isOpenBTS.value = false;
};
const showEvaluationofChannels = () => {
  isOpenEvaluationofChannels.value = true;
};
const hideEvaluationofChannelsMenu = () => {
  isOpenEvaluationofChannels.value = false;
};
const showVisitPlanManagement = () => {
  isOpenVisitPlanManagement.value = true;
};
const hideVisitPlanManagement = () => {
  isOpenVisitPlanManagement.value = false;
};
const userStore = useUserStore();
</script>
<template>
  <div class="onhover-dropdown navs-dropdown">
    <button
      class="btn btn-primary onhover-dropdown"
      data-bs-original-title=""
      title=""
    >
      {{ $t("MENU") }}
    </button>
    <div class="onhover-show-div onhover-show-div-list-page">
      <ul class="icon-lists border navs-icon">
        <li v-if="userStore.checkRoleMenuLatLong">
          <i class="fa fa-location-arrow"></i
          ><router-link
            class="dropdown-item"
            to="/location-update-objectType"
            >{{ $t("CHANNEL_LOCATION") }}</router-link
          >
        </li>
        <li v-if="userStore.checkRoleMenuCheckList">
          <i class="fa fa-pencil fa-fw"></i
          ><router-link class="dropdown-item" to="/searchChecklist">{{
            $t("CHANNEL_CHECKLIST")
          }}</router-link>
        </li>
        <li v-if="userStore.checkRoleMenuGetLog">
          <i class="fa fa-book"></i
          ><router-link class="dropdown-item" to="/getLog">{{
            $t("GET_LOG")
          }}</router-link>
        </li>
        <!-- <li>
          <i class="fa fa-paper-plane-o"></i><router-link class="dropdown-item" to="/visitPlan">{{ $t("VISIT_MANAGEMENT") }}</router-link>
        </li> -->
        <li>
          <i class="fa fa-area-chart"></i
          ><router-link class="dropdown-item" to="/channelManager">{{
            $t("CHANNEL_MANAGER")
          }}</router-link>
        </li>
        <li>
          <i class="fa fa-area-chart"></i
          ><router-link class="dropdown-item" to="/viewChannelBranch">{{
            $t("BRANCH_MANAGE")
          }}</router-link>
        </li>
        <li>
          <i class="fa fa-area-chart"></i
          ><router-link class="dropdown-item" to="/viewChannelBC">{{
            $t("BC_MANAGE")
          }}</router-link>
        </li>
        <li v-if="userStore.checkRoleMenuStrategy">
          <i class="fa fa-outdent"></i
          ><router-link class="dropdown-item" to="/strategyPlan">{{
            $t("SALE_STRATEGY_PLAN")
          }}</router-link>
        </li>
        <li>
          <i class="fa fa-outdent"></i
          ><router-link class="dropdown-item" to="/viewBranchPlan">{{
            $t("SALES_BRANCH_PLAN")
          }}</router-link>
        </li>
        <li>
          <i class="fa fa-outdent"></i
          ><router-link class="dropdown-item" to="/viewBusinessCenterPlan">{{
            $t("SALES_BUSINESS_CENTER_PLAN")
          }}</router-link>
        </li>
        <li @mouseenter="showBTSMenu" @mouseleave="hideBTSMenu">
          <i class="fa fa-hourglass"></i>
          <span class="btn-dropdown dropdown-toggle">{{ $t("BTS") }}</span>
          <ul
            class="onhover-dropdown-item"
            aria-labelledby="dropdownMenuBTS"
            v-if="isOpenBTS"
          >
            <li>
              <router-link class="dropdown-item" to="/btsManagement">{{
                $t("BTS_MANAGER")
              }}</router-link>
            </li>
            <li>
              <router-link class="dropdown-item" to="/salepolicyManagement">{{
                $t("SALES_POLICY_MANAGEMENT")
              }}</router-link>
            </li>
          </ul>
        </li>
        <li
          @mouseenter="showVisitPlanManagement"
          @mouseleave="hideVisitPlanManagement"
        >
          <i class="fa fa-hourglass"></i>
          <span class="btn-dropdown dropdown-toggle">{{
            $t("VISIT_MANAGEMENT")
          }}</span>
          <ul
            style="margin-left: -160px"
            class="onhover-dropdown-item"
            aria-labelledby="dropdownMenuBTS"
            v-if="isOpenVisitPlanManagement"
          >
            <li>
              <router-link class="dropdown-item" to="/care-plan">{{
                $t("CARE_PLAN")
              }}</router-link>
            </li>
            <li>
              <router-link class="dropdown-item" to="/survey-config">{{
                $t("SURVEY_CONFIG")
              }}</router-link>
            </li>
          </ul>
        </li>
        <li
          @mouseenter="showEvaluationofChannels"
          @mouseleave="hideEvaluationofChannelsMenu"
        >
          <i class="fa fa-hourglass"></i>
          <span class="btn-dropdown dropdown-toggle">{{
            $t("EVALUATION_OF_CHANNELS")
          }}</span>
          <ul
            style="margin-left: -160px"
            class="onhover-dropdown-item"
            aria-labelledby="dropdownMenuBTS"
            v-if="isOpenEvaluationofChannels"
          >
            <li>
              <router-link class="dropdown-item" to="/evaluation">{{
                $t("EVALUATION")
              }}</router-link>
            </li>
            <li>
              <router-link class="dropdown-item" to="/item-evaluation-config">{{
                $t("ITEMS_EVALUATION")
              }}</router-link>
            </li>
            <li>
              <router-link class="dropdown-item" to="/auditorEvaluation">{{
                $t("AUDITTORS_EVALUATION")
              }}</router-link>
            </li>
            <li>
              <router-link class="dropdown-item" to="/manageEvalution">{{
                $t("MANAGEMENT_EVALUTION")
              }}</router-link>
            </li>
            <li>
              <router-link class="dropdown-item" to="/penalty-config">{{
                $t("PENALTY_CONFIG")
              }}</router-link>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.btn-dropdown {
  background-color: transparent !important;
  color: #000;
  border: none;
}
li {
  padding: 9px 5px;
  display: flex !important;

  i {
    padding: 0px 5px;
  }
}
.onhover-show-div.onhover-show-div-list-page {
  top: 45px;
}
.onhover-dropdown {
  position: relative;
}
.onhover-dropdown-item {
  position: relative;
  margin-top: 20px;
  margin-left: -44px;
}

.onhover-dropdown .onhover-show-div {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
}

.onhover-dropdown ul {
  flex-direction: column;
}

.onhover-dropdown:hover .onhover-show-div {
  display: block;
}
</style>
