import React from 'react';

class Employee extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <tr>
              <td>{ this.props.employee.firstName }</td>
              <td>{ this.props.employee.lastName }</td>
              <td>{ this.props.employee.description }</td>
            </tr>
        );
    }

}

export default Employee;
