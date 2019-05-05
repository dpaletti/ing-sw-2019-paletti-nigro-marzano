package it.polimi.se2019.view;

public class UsernameResetEven extends VCEvent {
        private String password = null;

        public void setPassword(String password) {
                if(this.password == (null))
                        this.password = password;
        }
}
