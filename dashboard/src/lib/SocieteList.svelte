<script>
    import { onMount } from 'svelte';
    import {idSoc} from "./store.js";
 
    let societies = [];

    onMount(async () => {
        const response = await fetch('http://localhost:8080/societs');
        societies = await response.json();
    });

    function addTransaction(event) {
        const idSociet = event.target.dataset.idSociet;
        idSoc.set(idSociet);
    }
</script>

<style>
    .society-list {
        display:flex;
        flex-wrap: wrap;
        gap: 1rem;
        padding: 1rem;
    }

    .society-item {
        width: 15%;
        padding: 0.5rem;
        font-size: x-small;
        border: 1px solid #ccc;
        border-radius: 8px;
        background-color: #347ae494;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .society-item h2 {
        margin: 0;
        font-size: 1.5rem;
        color: #333;
    }

    .society-item p {
        margin: 0.5rem 0 0;
        color: #666;
    }
</style>

<div class="society-list">
    {#each societies as society}
      <button on:click={addTransaction} class="society-item" data-id-societ={society.id}>
        <h3>{society.id}</h3>
      </button>
    {/each}
</div>