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
      console.log("$$$$$$$$$$$$$$$$$$$$$");
      console.log(selectedSocietyId);
      console.log("$$$$$$$$$$$$$$$$$$$$$"); 
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

        eventSource = new EventSource('http://localhost:8080/events/SG');

        eventSource.onmessage = (event) => {
          console.log(data);
          updateChart(parseFloat(event.data)/10);
        };

        eventSource.onerror = (error) => {
          console.error('EventSource failed:', error);
          eventSource.close();
        };
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