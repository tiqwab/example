import React from 'react';
import UpdateDialog from './UpdateDialog';

class Employee extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.employee);
    }

    render() {
        return (
            <tr>
              <td>{ this.props.employee.firstName }</td>
              <td>{ this.props.employee.lastName }</td>
              <td>{ this.props.employee.description }</td>
              <td>{ this.props.employee.manager.name }</td>
              <td>
                <UpdateDialog employee={ this.props.employee }
                              attributes={ this.props.attributes }
                              onUpdate={ this.props.onUpdate } />
              </td>
              <td>
                <button onClick={ this.handleDelete }>Delete</button>
              </td>
            </tr>
        );
    }

}

Employee.propTypes = {
    onDelete: React.PropTypes.func,
    onUpdate: React.PropTypes.func,
    employee: React.PropTypes.object,
    attributes: React.PropTypes.array,
}

export default Employee;
