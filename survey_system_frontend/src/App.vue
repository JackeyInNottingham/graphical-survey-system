<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',

  created() {
    if (sessionStorage.getItem('store')) {
      this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(sessionStorage.getItem('store'))));
      sessionStorage.removeItem('store');
    }

    window.addEventListener('beforeunload', () => {
      sessionStorage.setItem('store', JSON.stringify(this.$store.state));
    });
  }
}
</script>

<style>
/* enable the height to 100% in child components */
html,body,#app{
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}
</style>
