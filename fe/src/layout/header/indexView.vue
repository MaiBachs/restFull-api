<template>
  <div class="main-header-right row m-0" style="background-color: #fef200">

    <div class="main-header-left" style="padding: 2px 40px">
      <SideBar />
    </div>
    <div class="nav-right col pull-right right-menu p-0">
      <ul class="nav-menus">
        <!-- <li class="onhover-dropdown">
          <LanguageItems />
        </li> -->
        <li class="onhover-dropdown">
          <ListPage />
        </li>
        <li class="onhover-dropdown">
          <LanguageItems />
        </li>
        <li class="onhover-dropdown">
          <ChatView />
        </li>
        <li class="onhover-dropdown p-0">
          <LogOut />
        </li>

      </ul>
    </div>
    <div class="d-lg-none mobile-toggle pull-right w-auto"><i data-feather="more-horizontal"><vue-feather
          type="more-horizontal" @click="collapseFilter()"></vue-feather></i></div>
  </div>
</template>
<script>
import { mapState } from 'vuex';
import SideBar from "./sidebarView.vue"
import LogOut from "./LogOut.vue"
import ChatView from "./ChatView.vue"
import ListPage from './ListPage.vue';
import LanguageItems from './LanguageItems.vue';


export default {
  components: {
    SideBar,
    LogOut,
    ChatView,
    ListPage,
    LanguageItems
},
  data() {
    return {
      bookmark: false,
      toogle: false,
      filtered: false,
    };
  },
  computed: {
    ...mapState({
      menuItems: (state) => state.menu.searchData,
      megamenuItems: (state) => state.menu.megamenu,
      searchOpen: (state) => state.menu.searchOpen,
    }),
  },
  methods: {
    search_open() {
      this.$store.state.menu.searchOpen = true;
    },
    collapseFilter() {
      this.filtered = !this.filtered;
    },
    bookmark_open() {
      this.bookmark = !this.bookmark;
    },
  },
}
</script>
<style lang="scss" scoped>
.page-main-header .main-header-right {
  .main-header-left {
    display: flex;
    justify-content: center;
    padding: 10px 40px;
  }
}
.page-main-header .main-header-right .nav-right .nav-menus {
  padding: 15px 30px;
}
</style>