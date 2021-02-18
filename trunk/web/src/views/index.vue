<template>
  <div class="app-container">Loading...</div>
</template>
 
<script>
import { mapState } from "vuex";

export default {
  name: "Index",
  components: {},
  created() {
    this.$router.push({ path: this.$store.state.permission.defaultPath });
    
  },
  data() {
    return {};
  },
  methods: {}
};
</script>

<style lang="scss" scoped>
</style>
