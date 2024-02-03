<script setup>
// import axios from "axios"
// import moment from 'moment'
// import Multiselect from "@vueform/multiselect";
import {ref, reactive} from "vue"
import { i18n } from '../../main.js';
import { useLoaddingStore } from "../../store/LoaddingStore";
// import { getBranch } from '../../store/getBranch'
import axios from "axios";
// import { getChannelType } from '../../store/getChannelType'
import { notify } from "@kyvg/vue3-notification";
const locationEdit = ref({})
const dataChannel = ref({})
// import Pagination from 'v-pagination-3';
// import { getShopBc } from '../../store/getBC';
// import { getStaffOwner } from '../../store/getOwnerStaff';
import LocationEdit from "./LocationEdit.vue";
// import PopupModalError from "../common/popupModalError.vue";
// const listShopBc = getShopBc();
// const listStaffOwner = getStaffOwner();
const loaddingStore = useLoaddingStore()
const listItem = ref([])
// const listBranch = getBranch();
// const listChannelType = getChannelType();
const checkResult = ref(true);
function myCallback(newPage) {
  // console.log(newPage)
   searchLocation(newPage)
}
const searchData = reactive({
  branch_id: "",
  branch_code: "",
  business_code: "",
  business_id: "",
  owner_code: "",
  owner_id: "",
  channel_type_id: "",
  channel_type_code: "",
  channel_code_id: "",
  channel_code_code: "",
  current_page: 0
});


function isNullOrEmpty(value) {
  return value === null || value === "";
}

// -----------------fill branch--------------------

// const suggestedBranch = ref([]);


// const selectBranch = (branch) => {
//   // console.log(branch)
//   searchData.business_id = ""
//   searchData.business_code = ""
//   searchData.channel_type_code = ""
//   searchData.channel_type_id = ""
//   searchData.channel_code_code = ""
//   searchData.channel_code_id = ""
//   searchData.owner_code = ""
//   searchData.owner_id = ""
//   suggestedBranch.value = [];
//   listStaffOwner.listStaffOwner = []
//   listShopBc.listShopBc = [];
//   listShopBc.actionGetShopBc(branch);
//   listStaffOwner.actionGetStaffOwner(branch)
//   listChannelType.getAllChannelType;
//   showList.value = false;
//   // console.log(searchData.branch_id)
//   checkResult.value = true;
//   dataChannel.value = []
//   // console.log(searchData)
// };

// const objectType = ref("");

// const searchInputChannelCode = ref("");

const paginate = reactive ({
  totalPage:"",
  currentPage:"",
  totalRecord: "",
  page_size:15
})

const showList = ref(false);
const searchLocation = async () =>{
  // if(listStaffOwner.listChannelCode.length){
  //   listStaffOwner.actionGetChanel(searchData)
  // }
  if(isNullOrEmpty(searchData.channel_code_code) || searchData.channel_code_code.length < 5){
    notify({
      type: 'error',
      title: i18n.global.t('VISIT_PLAN.IMPORT.ERROR.SELECT_SEARCH'),
      text: i18n.global.t('PLEASE_INPUT_CHANNEL_CODE'),
      duration: 2500,
    });
  // }else if(isNullOrEmpty(searchData.channel_type_id)){
  //   notify({
  //     type: 'error',
  //     title: i18n.global.t('VISIT_PLAN.IMPORT.ERROR.SELECT_SEARCH'),
  //     text: i18n.global.t('REQUIRE_CHANNEL_TYPE'),
  //     duration: 2500,
  //   });
  }else{
    searchData.current_page =  searchData.current_page > 1 ?searchData.current_page: 1;
    loaddingStore.loading = true;
    // if(searchData.channel_type_id.objectType == 1){
      // const shopChannel = await axios.post("/api/shop/get-shop-location",{
      //   code: searchInputChannelCode.value,
      //   shop_id: searchData.business_id?searchData.business_id:searchData.branch_id,
      //   channel_type_id : searchData.channel_type_id.id,
      //   owner_id: searchData.owner_id,
      //   page_size: 5,
      //   current_page: searchData.current_page
      // });
      // console.log(shopChannel)
      // if(shopChannel.data.content.length){
      //   checkResult.value = true;
      //   dataChannel.value = shopChannel.data.content;
      //   searchData.current_page = shopChannel.data.pageable.page_number + 1;
      //   paginate.totalPage = shopChannel.data.total_pages;
      //   paginate.totalRecord = shopChannel.data.total_elements;
      // }else{
      //   checkResult.value = false;
      //   dataChannel.value = []
      // }
    // }
    // else{
      const staffChanel = await axios.post("/api/employees/list-channels-with-lat-long",{
        code: searchData.channel_code_code,
        shop_id:  searchData.business_id?searchData.business_id:searchData.branch_id,
        owner_id: searchData.owner_id,
        channel_type_id : searchData.channel_type_id.id,
        object_type: searchData.channel_type_id.objectType,
        page_size: paginate.page_size,
        current_page: searchData.current_page
      });
      if(staffChanel.data.content.length){
        checkResult.value = true;
        dataChannel.value = staffChanel.data.content;
        searchData.current_page = staffChanel.data.pageable.page_number + 1;
        paginate.totalPage = staffChanel.data.total_pages;
        paginate.totalRecord = staffChanel.data.total_elements;
      }else{
        checkResult.value = false;
        dataChannel.value = []
      }
    // }
    loaddingStore.loading = false;
    // console.log(dataChannel.value)
  }
  showList.value = true;
}
const originItem = ref({});
// const formUpdateLocation = reactive({
//   channel_code_id: "",
//   name:"",
//   longitude: "",
//   latitude: "",
//   radius: "",
//   code:""
// });
const showPopupEdit = (data) => {
  locationEdit.value =  {...data};
  listItem.value = data
  originItem.value = data
  console.log(data)
  // locationEdit.value = [];
  // if(objectType.value == 2){
  //   formUpdateLocation.channel_code_id = data.id
  //   formUpdateLocation.name = data.name
  // }else if(objectType.value == 1){
  //   formUpdateLocation.channel_code_id = data.shop_id
  //   formUpdateLocation.name = data.shop_code
  // }else{
  //   formUpdateLocation.channel_code_id = data.id
  // }
  // formUpdateLocation.longitude = data.longitude
  // formUpdateLocation.latitude = data.latitude
  // formUpdateLocation.radius = data.radius
  // formUpdateLocation.code = data.code
  // locationEdit.value =  {...formUpdateLocation};
  // listItem.value = formUpdateLocation
  // originItem.value = formUpdateLocation
};

// const canOk = formUpdateLocation.longitude != null || formUpdateLocation.latitude != null || formUpdateLocation.radius != null

// async function updateLocation(actions) {
//   const longitude = parseFloat(locationEdit.value.longitude);
//   const latitude = parseFloat(locationEdit.value.latitude);

//   if (
//     !isNaN(longitude) &&
//     !isNaN(latitude) &&
//     longitude >= -180 &&
//     longitude < 180 &&
//     latitude >= -90 &&
//     latitude < 90
//   ) {
//   if (locationEdit.value.channel_code_id != null) {
//     try {
//       loaddingStore.loading = true;
//       const response = await axios.post("/api/Location-manager/update-location", locationEdit.value);
//       console.log(response.data)
//       loaddingStore.loading = false;
//       if(response.data == true){
//         notify({
//           type: "success",
//           title: i18n.global.t('EDIT_SUCCESSS'),
//           text: i18n.global.t('EDIT_SUCCESSS') + " " + locationEdit.value.name,
//           duration: 1700,
//         });
//         actions.onClose();
//       }else{
//         notify({
//           type: "error",
//           title: i18n.global.t('EDIT_ERROR'),
//           text: i18n.global.t('EDIT_ERROR') + " " +locationEdit.value.name,
//           duration: 1700,
//         });
//       }
//       searchLocation();
//     } catch (error) {
//       loaddingStore.loading = false;
//       notify({
//         type: "error",
//         title: "Get current user",
//         text: error,
//         duration: 1700,
//       });
//     }
//   }
// } else {
//     notify({
//       type: "error",
//       title: "Invalid Coordinates",
//       text: "Please enter valid longitude (-180 to 179.99) and latitude (-90 to 89.99).",
//       duration: 1700,
//     });
//   }

// }
// export default {
//     data(){
//         return{
//           errors: [],
//           formSubmitted:false,
//           firstname:"mark",
//           firstError:false,
//           lastname:"Otto",
//           lastError:false,
//           username:"",
//           userError:false,
//           city:"",state:"",zip:'',
//           cityError:false, editItem.longitude:"", editItem.latitude: "",
//           stateError:false,
//           zipError:false,
//    }
//     },
//         methods:{
//       onCustomStyleSubmit(){
//          this.formSubmitted = true;
//         this.errors = [];
// 				if (this.firstname.length<3||this.firstname>10 && this.lastname.length<3 || this.lastname.length>10  ) {
// 					this.firstError = true;
//           this.lastErrorl=true;
// 					this.errors.push({
// 					});
// 				} else {
// 				this.firstError = false;
//         this.lastError=false;
// 					this.errors.push({
// 						'message': 'Look good!'
// 					});
// 				}
//         if(this.username.length<3|| this.username.length>6 && this.city.length<3|| this.city.length>10 && this.state.length<7 && this.zip<8 ){
//           this.userError=true;
//           this.cityError=true;
//           this.stateError=true;
//           this.zipError=true;
//           this.errors.push({
//              	'message1': 'Please choose a unique and valid username.', 'message2': 'Please provide a valid city.', 'message3': 'Please select a valid state.', 'message4': 'Please provide a valid zip.',
// 					});
//         }
//         else{
//           this.userError=false;
//           this.cityError=false;
//           this.stateError=false;
//           this.zipError=false;
//         }
//       },

//         }
// }
function clearData() {
  searchLocation()
}
function changeChannelCode() {
  searchData.current_page = 1
}
</script>

<template>
  <div class="card-body p-0">
    <!-- <div class="row mb-3"> -->
      <!-- <div class="col align-self-start"> -->
        <!-- <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.COMBOBOX.BRANCHE") }}: </label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.branch_id"
              :options="listBranch.getBranch || []"
              track-by="name"
              @clear="removeBr"
              :placeholder="i18n.global.t('INPUT.VALUE')"
            ></Multiselect>
          </div>
        </div>
      </div>
    

      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-4 col-form-label"
            >{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}:</label
          >
          <div class="col-sm-8">
            <Multiselect
              v-model="searchData.channel_type_id"
              :options="listChannelType.listAllChannelType || []"
              track-by="name"
              :placeholder="i18n.global.t('INPUT.VALUE')"
            ></Multiselect>
          </div>
        </div>
      </div>

    </div> -->

    <div class="row mb-3">
      


      <div class="col align-self-start">
        <div class="mb-3 row">
          <label class="col-sm-3 col-form-label"
            >{{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE") }}:<span style="font-weight: bold; color: red;">*</span></label
          >
          <div class="col-sm-8">
            <input v-model="searchData.channel_code_code" class="form-control" @change="changeChannelCode"/>
          </div>
        </div>
      </div>

      <div class="col align-self-start">
        <button
          class="btn btn-pill btn-primary-gradien"
          type="button"
          @click="searchLocation()"
        >
        {{ $t("SEARCH") }}
        </button>
      </div>
    </div>

    <div class="row mb-3 evaluations">
      <fieldset class="table-responsive"  v-if="!checkResult">
        <div class="text-center no-item">{{$t("NO.ITEM")}}</div>
      </fieldset>
      <fieldset class="table-responsive" v-if="dataChannel.length">
        <div class="table-responsive">
          <table class="table table-bordered table-hover">
            <thead class="table-success" style="background-color: #24695c; color: #fff;">
              <tr>
                <th scope="col">#</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_CODE") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION.SALE_INFO.CHANNEL_NAME") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.TYPE_CHANNEL_TYPE") }}</th>
                  <th scope="col">{{ $t("STATUS") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.COMBOBOX.OWNER") }}</th>
                  <th scope="col">{{ $t("LOCATION.LATIT") }}</th>
                  <th scope="col">{{ $t("LOCATION.LONGIT") }}</th>
                  <th scope="col">{{ $t("LOCATION.RADIUS") }}</th>
                  <th scope="col">{{ $t("HOME.SEARCH.MAP.ACTION") }}</th>
                </tr>
              </thead>
              <tbody v-for="(item ,index) in dataChannel" :key="item">
                <tr>
                  <td>{{(searchData.current_page-1)*paginate.page_size +(index+1) }}</td>
                  <td>{{ item.shop_code?item.shop_code:item.code }}</td>
                  <td>{{ item.shop_name?item.shop_name:item.name  }}</td>
                  <td>{{ item.channel_type_name }}</td>
                  <td>{{ item.status==1?$t("ACTIVE"): $t("INACTIVE")  }}</td>
                  <td>{{ item.staff_owner_name?item.staff_owner_name:item.staff_owner_code  }}</td>
                  <td>{{ item.latitude  }}</td>
                  <td>{{ item.longitude  }}</td>
                  <td>{{ item.radius  }}</td>
                  <td>
                    <div class="buttons">
                      <button
                      class="btn btn-primary btn-xs"
                      data-bs-toggle="modal"
                      data-original-title="test"
                      data-bs-target="#exampleModal"
                      @click="showPopupEdit(item)"
                    >
                    <i class="fa fa-edit"></i>
                      <!-- {{ $t('EDIT') }} -->
                    </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="pagination-search">
              <pagination v-model="searchData.current_page" :records="paginate.totalRecord" :per-page="paginate.page_size" @paginate="myCallback"/>
            </div>
        </div>
      </fieldset>
    </div>
  </div>

  <LocationEdit  @clearValue="clearData" v-model="locationEdit"  :listItem="listItem"/>
  <popupModalError modal-id="popupError" title="Error">
    <errorEditEvaluation />
  </popupModalError>
</template>

<style lang="scss" scoped>
.multiselect {
  max-height: 35px;
  min-height: 30px;
}
.dp__input_wrap {
  height: 35px;
  display: flex;
}
.evaluations {

  fieldset {
    padding: 15px;
    margin: 0 2px;
    border: 1px solid #c0c0c0;
  }
  legend {
    float: initial;
    font-size: 14px;
    width: auto;
    border-bottom: none;
    padding: 0px 5px;
  }
  .no-item {
    font-weight: 500;
  }
  svg {
    width: 16px;
    height: 16px;
  }
}
.pagination-search {
  float: right;
}

.suggested-list{
  position: absolute;
  right: 0;
}
</style>