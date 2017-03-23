import React from 'react';
import axios from 'axios';
import EmployeeList from './EmployeeList';

const client = axios.create({
    // baseURL: 'http://localhost:8080/api/'
});

client.interceptors.response.use( (response) => {
    return response;
}, (error) => {
    return Promise.reject(error);
});

const root = "http://localhost:8080/api/";

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            employees: [],
        };
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }

    loadFromServer(pageSize) {
        client.get('employees', {
            baseURL: root,
            params: {
                size: pageSize,
            },
        })
        .then(employeeCollection => {
            console.log(employeeCollection);
            return client.get(employeeCollection.data._links.profile.href, {
                headers: {
                    Accept: 'application/schema+json',
                },
            })
            .then(schema => {
                console.log(schema);
                this.schema = schema.data;
                return employeeCollection;
            });
        })
        .then(employeeCollection => {
            console.log(employeeCollection);
            this.setState({
                employees: employeeCollection.data._embedded.employees,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: employeeCollection.data._links,
            });
        })
        .catch(response => {
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
