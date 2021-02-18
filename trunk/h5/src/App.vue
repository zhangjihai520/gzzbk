<template>
  <div id="app">
    <router-view/>
    <nav-bar v-show="$route.meta.navShow"/>
  </div>
</template>

<script>
import navBar from './components/nav/navBar'
export default {
  name: 'App',
  components:{
    navBar
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
}
</style>
