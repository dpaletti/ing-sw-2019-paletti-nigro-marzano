package it.polimi.se2019.model.MVEvents;


import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class UsernameEvaluationEvent extends MVEvent {
        private boolean validUsername;
        private List<String> nonAvailableUsernames;
        private String password;

        public UsernameEvaluationEvent(String destination, List<String> nonAvailableUsernames){
                super(destination);
                this.nonAvailableUsernames = nonAvailableUsernames;
                validUsername = false;
                this.password = null;

        }

        public UsernameEvaluationEvent (String destination, String password){
                super(destination);
                validUsername = true;
                this.password = password;
        }

        public List<String> getNonAvailableUsernames() {
                return new ArrayList<>(nonAvailableUsernames);
        }

        public boolean isValidUsername() {
                return validUsername;
        }

        public String getPassword() {
                return password;
        }
}
