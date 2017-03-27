import React from 'react';
import axios from 'axios';
import EmployeeList from './EmployeeList';
import CreateDialog from './CreateDialog';

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
            attributes: [],
            pageSize: 2,
            links: {},
        };

        this.onNavigate = this.onNavigate.bind(this);
        this.updatePageSize = this.updatePageSize.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onUpdate = this.onUpdate.bind(this);
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
                this.links = employeeCollection.data._links;
                return employeeCollection;
            });
        })
        .then(employeeCollection => {
            axios.all(employeeCollection.data._embedded.employees.map(employee => {
                return client.get(employee._links.self.href);
            }))
            .then(employees => {
                console.log(employees);
                this.setState({
                    employees: employees.map(emp => {
                        emp.data.headers = emp.headers;
                        return emp.data;
                    }),
                    attributes: Object.keys(this.schema.properties),
                    pageSize: pageSize,
                    links: this.links,
                });
            })
            .catch(response => {
                console.error(response);
            });
        })
        .catch(response => {
            console.error(response);
        });
    }

    onCreate(newEmployee) {
        client.get('employees', {
            baseURL: root,
        })
        .then(employeeCollection => {
            return client.post(employeeCollection.data._links.self.href, newEmployee, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
        })
        .then(response => {
            console.log(response);
            return client.get('employees', {
                baseURL: root,
                params: {
                    size: this.state.pageSize,
                },
            });
        })
        .then(response => {
            this.onNavigate(response.data._links.last.href);
        })
        .catch(response => {
            console.error(response);
        });
    }

    onNavigate(navUri) {
        client.get(navUri)
        .then(employeeCollection => {
            axios.all(employeeCollection.data._embedded.employees.map(employee => {
                return client.get(employee._links.self.href);
            }))
            .then(employees => {
                console.log(employees);
                this.setState({
                    employees: employees.map(emp => {
                        emp.data.headers = emp.headers;
                        return emp.data;
                    }),
                    attributes: this.state.attributes,
                    pageSize: this.state.pageSize,
                    links: employeeCollection.data._links,
                });
            })
            .catch(response => {
                console.error(response);
            });
        })
        .catch(response => {
            console.error(response);
        });
    }

    onUpdate(employee, updatedEmployee) {
        console.log(employee);
        client.put(employee._links.self.href, updatedEmployee, {
            headers: {
                'Content-Type': 'application/json',
                'If-Match': employee.headers.etag,
            },
        })
        .then(response => {
            console.log(response);
            this.loadFromServer(this.state.pageSize);
        }, error => {
            console.log(error.message);
            if (error.response) {
                if (error.response.status === 412) {
                    alert('DENIED: Unable to update' + employee._links.self.href + '. Your copy is stale.');
                }
            }
        });
    }

    onDelete(employee) {
        client.delete(employee._links.self.href)
        .then(response => {
            this.loadFromServer(this.state.pageSize);
        })
        .catch(response => {
            console.error(response);
        });
    }

    updatePageSize(pageSize) {
        if (pageSize != this.state.pageSize) {
            this.loadFromServer(parseInt(pageSize));
        }
    }

    render() {
        return (
            <div>
                <CreateDialog attributes={ this.state.attributes }
                              onCreate={ this.onCreate } />
                <EmployeeList employees={ this.state.employees }
                              links={ this.state.links }
                              pageSize={ this.state.pageSize }
                              onNavigate={ this.onNavigate }
                              updatePageSize={ this.updatePageSize }
                              onDelete={ this.onDelete }
                              attributes={ this.state.attributes }
                              onUpdate={ this.onUpdate }/>
            </div>
        );
    }

}

export default App;
