<script setup>
import { useUserStore } from '../../store/UserStore';
import Multiselect from "@vueform/multiselect";
const userStore = useUserStore()
import VueDatePicker from "@vuepic/vue-datepicker";

import { ref } from 'vue';
const openSidebar = ref(true);



</script>
<template>
    <header class="main-nav" :class="{ close_icon: !openSidebar }">
        <div class="input-group">
            <div class="d-flex codigo-de">
                <input type="text" class="form-control" placeholder="Codigo de canal" />
            </div>
            
            <div class="d-flex input-group-btn">
                <button id="search_btn" class="btn btn-info btn-xs" type="button">
                    <i class="fa fa-search icon-label-search"></i>
                </button>
                <button class="btn btn-danger btn-xs" type="button">
                    <i class="fa fa-times icon-label-search"></i>
                </button>
             </div>

        </div>
        
        <div class="main-navbar">
            <div id="mainnav aside">
                <div class="custom-scrollbar sidebar-main" style="display: block">
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                    <i class="fa fa-home icon-label-search"></i>
                    <label class="form-label-search"
                        >{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}:</label
                    >
                    </div>
                    <div class="col-sm-7">
                        <Multiselect
                          v-model="selectedBranche"
                          :options="userStore.listBranch || []"
                          track-by="code"
                          label="label"
                          @clear="clearDataBranch"
                          :canClear="false"
                          :value="selectedBranch"
                          :placeholder="$t('SELECT_ONE')"
                        ></Multiselect>
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-briefcase icon-label-search "></i>
                        <label class="form-label-search"
                            >{{ $t("HOME.SEARCH.COMBOBOX.BC") }}:</label
                        >
                    </div>
                    <div class="col-sm-7">
                        <Multiselect
                          v-model="selectedBC"
                          :options="userStore.listBC || []"
                          :placeholder="$t('SELECT_ONE')"
                          track-by="code"
                          label="label"
                          :canClear="false"
                          :searchable="false"
                          :allow-empty="false"
                        ></Multiselect>
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-users icon-common icon-label-search"></i>
                        <label class="form-label-search"
                            >{{ $t("HOME.SEARCH.COMBOBOX.TYPEUSER") }}:</label
                        >
                    </div>
                    <div class="col-sm-7">
                        <Multiselect
                          v-model="userType"
                          :options="userStore.listUserType || []"
                          :placeholder="$t('SELECT_ONE')"
                          track-by="code"
                          label="label"
                          :canClear="false"
                          :searchable="false"
                          :allow-empty="false"
                        ></Multiselect>
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-user-circle-o icon-label-search"></i>
                    <label class="form-label-search"
                        >{{ $t("HOME.SEARCH.COMBOBOX.ZONAL") }}:</label
                    >
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-cube icon-label-search"></i>
                    <label class="form-label-search"
                        >{{ $t("HOME.SEARCH.COMBOBOX.ZONE") }}:</label
                    >
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-paper-plane icon-label-search"></i>
                    <label class="form-label-search"
                        >{{ $t("HOME.SEARCH.COMBOBOX.PROVINCE") }}:</label
                    >
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-windows icon-label-search"></i>
                    <label class="form-label-search"
                        >{{ $t("HOME.SEARCH.COMBOBOX.DISTRICT") }}:</label
                    >
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>

                <div class="form-group d-flex m-b-10 take-care">
                    <label class="take-care-channel">{{ $t("TAKE.CARE.CHANNEL") }}</label>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-clock-o icon-label-search"></i>

                        <label class="form-label-search"
                            >{{ $t("HOME.SEARCH.COMBOBOX.DISTRICT") }}:</label
                        >
                    </div>
                    <div class="col-sm-7">
                    <VueDatePicker v-model="date" range :preset-dates="presetDates">
                        <template
                        #preset-date-range-button="{ label, value, presetDate }"
                        >
                        <span
                            role="button"
                            :tabindex="0"
                            @click="presetDate(value)"
                            @keyup.enter.prevent="presetDate(value)"
                            @keyup.space.prevent="presetDate(value)"
                        >
                            {{ label }}
                        </span>
                        </template>
                    </VueDatePicker>
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-map-marker icon-label-search"></i>
                        <label class="form-label-search">{{ $t("STATUS") }}:</label>
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-bars icon-label-search"></i>
                    <label class="form-label-search">{{ $t("TYPE") }}:</label>
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10 take-care">
                    <label class="take-care-channel">{{ $t("HOME.SEARCH.BTS.UP.TO") }}</label>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <label class="form-label-search">{{ $t("CREATE_DATE") }}:</label>
                    </div>
                    <div class="col-sm-7">
                    <VueDatePicker v-model="date" range :preset-dates="presetDates">
                        <template
                        #preset-date-range-button="{ label, value, presetDate }"
                        >
                        <span
                            role="button"
                            :tabindex="0"
                            @click="presetDate(value)"
                            @keyup.enter.prevent="presetDate(value)"
                            @keyup.space.prevent="presetDate(value)"
                        >
                            {{ label }}
                        </span>
                        </template>
                    </VueDatePicker>
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-sort-amount-desc icon-label-search"></i>
                    <label class="form-label-search"
                        >{{ $t("HOME.SEARCH.BTS.AMOUNT") }}:</label
                    >
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-flag icon-label-search"></i>
                        <label class="form-label-search"
                            >{{ $t("HOME.SEARCH.BTS.COLOR.YOUR.BASS") }}:</label
                        >
                    </div>
                    <div class="col-sm-7">
                        <input class="form-control" type="text" />
                    </div>
                </div>

                <div class="form-group d-flex m-b-10">
                    <input
                    id="chk-ani"
                    class="checkbox_animated"
                    type="checkbox"
                    checked=""
                    /><label class="form-label-search">{{
                    $t("SEARCH_SALES_PLAN")
                    }}</label>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                    <label class="form-label-search">{{ $t("POLICY") }}:</label>
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                    <label class="form-label-search"
                        >{{ $t("SALE_RESULT_DATE") }}:</label
                    >
                    </div>
                    <div class="col-sm-7">
                    <VueDatePicker v-model="date" range :preset-dates="presetDates">
                        <template
                        #preset-date-range-button="{ label, value, presetDate }"
                        >
                        <span
                            role="button"
                            :tabindex="0"
                            @click="presetDate(value)"
                            @keyup.enter.prevent="presetDate(value)"
                            @keyup.space.prevent="presetDate(value)"
                        >
                            {{ label }}
                        </span>
                        </template>
                    </VueDatePicker>
                    </div>
                </div>

                <div class="form-group take-care">
                    <label class="take-care-channel">{{ $t("OTHER.CRITERIA") }}</label>
                </div>

                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-maxcdn icon-label-search"></i>
                        <label class="form-label-search"
                            >{{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.ACTIVATION") }}:</label
                        >
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                <div class="form-group d-flex m-b-10">
                    <div class="col-sm-5 form-group-icon">
                        <i class="fa fa-usd icon-label-search"></i>
                        <label class="form-label-search"
                            >{{ $t("HOME.SEARCH.MAP.ANYPAY") }}:</label
                        >
                    </div>
                    <div class="col-sm-7">
                    <input class="form-control" type="text" />
                    </div>
                </div>
                </div>
            </div>
        <!-- <div id="right-arrow" class="right-arrow">
            <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="feather feather-arrow-right"
            >
            <line x1="5" y1="12" x2="19" y2="12"></line>
            <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
        </div> -->
        </div>
    </header>
</template>