import React from 'react';
import axios from 'axios';
import EmployeeList from './EmployeeList';

const client = axios.create({
    baseURL: 'http://localhost:8080'
});

client.interceptors.response.use( (response) => {
    return response;
}, (error) => {
    return Promise.reject(error);
});

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            employees: [],
        };
    }

    componentDidMount() {
        client.get('/api/employees')
          .then( (response) => {
              console.log(response);
              this.setState({
                  employees: response.data._embedded.employees,
              });
          })
          .catch( (response) => {
              console.error(response);
          });
    }

    render() {
        return (
            <EmployeeList employees={ this.state.employees } />
        );
    }

}

export default App;
