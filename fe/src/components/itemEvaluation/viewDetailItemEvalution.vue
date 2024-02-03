<script setup>
import { useItemConfigStore } from './../../store/ItemConfigStore';

const itemConfigStore = useItemConfigStore();
</script>
<template>
  <div
   
    class="row p-15"
  >
    <!-- <div class="row p-15"> -->
    <fieldset>
      <legend>{{ itemConfigStore.gravedad }}</legend>
      <fieldset class="m-p-20">
        <legend>{{ channelTypeEdit }}</legend>
        <fieldset class="m-p-20">
          <legend>{{ groupEdit }}</legend>
          <fieldset class="m-p-20">
            <legend>{{ itemEdit }}</legend>
            <div class="main-edit">
              <div class="item-top">
                <div class="item-top-left">
                  <legend>{{ $t("WEIGHT") }}</legend>
                  <legend>{{ $t("OPTIONS_OF_ANSWER") }}</legend>
                  <legend>{{ $t("VALIDATE_OF_EVIDENCE") }}</legend>
                  <legend>{{ $t("REFERENCIAL_PHOTO") }}</legend>
                  <legend>{{ $t("REASONS_OF_NOK") }}</legend>
                </div>
                <div class="item-top-right">
                  <legend>
                    {{ itemConfigStore.dataItemDetails * 100 }}%
                  </legend>
                  <legend class="btn-radio-item">
                    <label class="custom-control custom-checkbox">
                      <input type="checkbox"/>
                      <span class="custom-control-description">OK</span>
                    </label>
                    <label class="custom-control custom-checkbox">
                      <input
                        type="checkbox"
                        class="custom-control-input"
                      />
                      <span class="custom-control-description">NOK</span>
                    </label>
                    <label class="custom-control custom-checkbox">
                      <input
                        type="checkbox"
                        value="checkedValue"
                        class="custom-control-input"
                      />
                      <span class="custom-control-description">NA</span>
                    </label>
                  </legend>
                  <legend class="btn-radio-item">
                    <label class="custom-control custom-checkbox">
                      <input
                        type="checkbox"
                        name=""
                        id=""
                        value="checkedValue"
                        class="custom-control-input"
                      />
                      <span class="custom-control-description">Photo</span>
                    </label>
                    <label class="custom-control custom-checkbox">
                      <input
                        type="checkbox"
                        name=""
                        id=""
                        value="checkedValue"
                        class="custom-control-input"
                      />
                      <span class="custom-control-description">Audio</span>
                    </label>
                    <label class="custom-control custom-checkbox">
                      <input
                        type="checkbox"
                        name=""
                        id=""
                        value="checkedValue"
                        class="custom-control-input"
                      />
                      <span class="custom-control-description">File</span>
                    </label>
                    <label class="custom-control custom-checkbox">
                      <input
                        type="checkbox"
                        name=""
                        id=""
                        value="checkedValue"
                        class="custom-control-input"
                      />
                      <span class="custom-control-description">Video</span>
                    </label>
                  </legend>
                  <legend>imagen.jpg</legend>
                  <legend>Gravedad</legend>
                </div>
              </div>
              <div class="item-bot">
                <table class="grave">
                  <tr class="grave-item">
                    <td style="padding-left: 40px; font-weight: 500">
                      No implementado
                    </td>
                    <td style="padding-left: 120px; color: #4682b4">
                      Muy grave
                    </td>
                  </tr>
                  <tr class="grave-item">
                    <td style="padding-left: 40px; font-weight: 500">
                      No implementado
                    </td>
                    <td style="padding-left: 120px; color: #4682b4">
                      Muy grave
                    </td>
                  </tr>
                  <tr class="grave-item">
                    <td style="padding-left: 40px; font-weight: 500">
                      No implementado
                    </td>
                    <td style="padding-left: 120px; color: #4682b4">
                      Muy grave
                    </td>
                  </tr>
                  <tr class="grave-item">
                    <td style="padding-left: 40px; font-weight: 500">
                      No implementado
                    </td>
                    <td style="padding-left: 120px; color: #4682b4">
                      Muy grave
                    </td>
                  </tr>
                </table>
              </div>
            </div>
            <div v-if="itemEdit" class="row">
              <div class="text-center">
                <button
                  class="col-1 btn btn-danger btn-sm"
                  type="button"
                  @click="saveItem"
                >
                  {{ $t("SAVE") }}
                </button>
              </div>
            </div>
          </fieldset>
        </fieldset>
      </fieldset>
    </fieldset>
  </div>
</template>
