import React, { Component } from 'react'
import { CalendarContainer } from 'containers/Home'
import { Route } from 'react-router-dom';
import { AuthWrapper } from 'components/Auth';
export default class Home extends Component {
    render() {
        return (
            <AuthWrapper>
                <Route path="/home" component={CalendarContainer}/>
            </AuthWrapper>
        )
    }
}
