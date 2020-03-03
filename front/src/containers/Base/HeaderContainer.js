import React, { Component, Fragment } from 'react';
import Header, { LoginButton, LogoutButton } from 'components/Base/Header';
import { connect } from 'react-redux';
import * as userActions from 'redux/modules/user';
import { bindActionCreators } from 'redux';
import storage from 'lib/storage';



class HeaderContainer extends Component {

    handleLogout = async () => {
        // const { UserActions } = this.props;
        // try {
        //     await UserActions.logout();
        // } catch (e) {
        //     console.log(e);
        // }

        storage.remove('token');
        window.location.href = '/'; // 홈페이지로 새로고침
    }

    render() {
        const { visible, user, email } = this.props;
        console.log("email은 ~입니다", email)
        if(!visible) return null;



        return (
            <Header>
                { user.get('logged') 
                    ? (
                        <Fragment>
                    <div>{user.getIn(['loggedInfo', 'name'])}님 어서오세요.</div> <LogoutButton onClick={this.handleLogout}/>
                    </Fragment>)
                    : <LoginButton/> 
                }
            </Header>
        );
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['header', 'visible']),
        user: state.user,
        email: state.user.getIn(['loggedInfo', 'email'])
    }),
    (dispatch) => ({
        UserActions: bindActionCreators(userActions, dispatch)
    })
)(HeaderContainer);