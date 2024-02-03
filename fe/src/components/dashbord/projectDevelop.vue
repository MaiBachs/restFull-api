<template>
    <div class="col-sm-12">
                <div class="card">
                  <div class="card-header pb-0">
                    <h5>Kick start your project development !</h5>
                    <div class="setting-list">
                      <ul class="list-unstyled setting-option">
                        <li>
                          <div class="setting-primary"><i class="icon-settings"></i></div>
                        </li>
                        <li><i class="view-html fa fa-code font-primary"></i></li>
                        <li><i class="icofont icofont-maximize full-card font-primary"></i></li>
                        <li><i class="icofont icofont-minus minimize-card font-primary"></i></li>
                        <li><i class="icofont icofont-refresh reload-card font-primary"></i></li>
                        <li><i class="icofont icofont-error close-card font-primary"></i></li>
                      </ul>
                    </div>
                  </div>
                  <div class="card-body">
                    <p>Getting start with your project custom requirements using a ready template which is quite difficult and time taking process,
                       viho Admin provides useful features to kick start your project development with no efforts !</p>
                    <ul>
                      <li>
                        <p>viho Admin provides you getting start pages with different layouts, use the layout as per your custom requirements and just change the branding, menu & content.</p>
                      </li>
                      <li>
                        <p>Every components in viho Admin are decoupled, it means use only components you actually need!
                           Remove unnecessary and extra code easily just by excluding the path to specific SCSS, JS file.</p>
                      </li>
                      <li>
                        <p>It use PUG as template engine to generate pages and whole template quickly using node js.
                           Save your time for doing the common changes for each page (i.e menu, branding and footer) by generating template with pug.</p>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
               <div class="col-sm-12">
                <div class="card">
                  <div class="card-header pb-0">
                    <h5>What is starter kit ?</h5>
                    <div class="setting-list">
                      <ul class="list-unstyled setting-option">
                        <li>
                          <div class="setting-primary"><i class="icon-settings"></i></div>
                        </li>
                        <li><i class="view-html fa fa-code font-primary"></i></li>
                        <li><i class="icofont icofont-maximize full-card font-primary"></i></li>
                        <li><i class="icofont icofont-minus minimize-card font-primary"></i></li>
                        <li><i class="icofont icofont-refresh reload-card font-primary"></i></li>
                        <li><i class="icofont icofont-error close-card font-primary"></i></li>
                      </ul>
                    </div>
                  </div>
                  <div class="card-body">
                    <p>Starter kit is a set of pages with different layouts, useful for your next project to start development process from scratch with no time.</p>
                    <ul>
                      <li>
                        <p>Each layout includes basic components only.</p>
                      </li>
                      <li>
                        <p>Select your choice of layout from starter kit, customize it with optional changes like colors and branding, add required dependency only.</p>
                      </li>
                      <li>
                        <p> {{ userStore.currentUser.staff_code }} </p>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
</template>
<script setup>
import { useUserStore } from './../../store/UserStore';
const userStore = useUserStore()
</script>