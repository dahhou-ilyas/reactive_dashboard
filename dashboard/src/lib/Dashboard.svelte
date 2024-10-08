<script>
    import { onMount,onDestroy } from 'svelte';
    import Chart from 'chart.js/auto'
    import { idSoc } from './store.js';
    
    let selectedSocietyId;
    $: selectedSocietyId = $idSoc;
    
    let chart;

    let chartElement;

    let data = [];

    const maxDataPoints = 20; 

    let eventSource;

    $: if (selectedSocietyId) {
      console.log(selectedSocietyId);
      if (eventSource) {
        eventSource.close();
      }
      eventSource = new EventSource(`http://localhost:8080/stream/transactions/societe/${selectedSocietyId}`);
      eventSource.onmessage = (event) => {
        let price=JSON.parse(event.data).price;
        console.log(price);
        updateChart(price);
      };
      eventSource.onerror = (error) => {
        console.error('EventSource failed:', error);
        eventSource.close();
      };
    }



    function updateChart(newValue){
        if(data.length > maxDataPoints){
            data.shift();
        }
        data.push(newValue);
        chart.data.labels = data.map((_, index) => index.toString());
        chart.data.datasets[0].data = data;
        chart.update();
    }

    onMount(()=>{
        chart = new Chart(chartElement,{
            type: 'line',
            data: {
              labels: [],
              datasets: [{
                label: 'Données en temps réel',
                data: data
              }]
            },
            options: {
              responsive: true,
              animation: {
                duration: 0
              },
              scales: {
                y: {
                  beginAtZero: true,
                  max: 1000
                }
              }
            }
        });
    });
    onDestroy(() => {
    if (eventSource) {
      eventSource.close();
    }
    if (chart) {
      chart.destroy();
    }
  });
</script>

<canvas bind:this={chartElement}></canvas>