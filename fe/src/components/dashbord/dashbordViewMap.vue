<template>
  <Breadcrumbs title="Layout Light" />
  <vue_leaflet />
  <div class="widgets">
    <div class="row">
      <div class="col-sm-12">
        <div class="card">
          <div class="card-header pb-0">
            <h1>{{ $t('Welcome') }}</h1>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import vue_leaflet from "./vue_leaflet.vue"
</script>