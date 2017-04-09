import axios from 'axios';

console.log('hello');

const client = axios.create({
    baseURL: 'http://localhost:8080/',
});

client.get('/resource')
.then(response => {
    console.log(response);
})
.catch(response => {
    console.error(response);
});
