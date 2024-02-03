<template>
  <div class="page-wrapper compact-wrapper">
    <div class="page-main-header">
      <Header />
    </div>
    <div class="page-body-wrapper horizontal-menu">
      <!-- <div class="main-nav close_icon"></div> -->
      <div class="page-body">
        <div class="container-fluid">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>


</template>

<script setup>
import Header from './header/indexView.vue';
import { useUserStore } from '../store/UserStore';
import { getBranch } from '../store/getBranch';
import { getChannelType } from '../store/getChannelType';
import { getlistPolicy }  from '../store/getlistPolicy';
// import { getlistLog }  from '../store/getlistLog';
// const listgetLog = getlistLog();
// listgetLog.actionGetlistLog()

const userStore = useUserStore()
userStore.fetchCurrentUser()
const getListChannelType = getChannelType();
getListChannelType.actionGetChannelType();
const getListBranch = getBranch();
getListBranch.actionGetBranch();
const listfillPolicy = getlistPolicy();
listfillPolicy.actionGetlistPolicy()


console.log(listfillPolicy.listgetPolicy)
console.log(listfillPolicy.listgetPolicy)

</script>
<style lang="scss" scoped>
.page-wrapper.compact-wrapper .page-body-wrapper .page-body {
  margin-left: 0px;
  padding-top: 15px;
}
</style>