/**
 Copyright 2016 Bernd Porr, mail@berndporr.me.uk

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **/


package tech.glasgowneuro.attysecg_hrv;

/**
 * Simple 1st order highpass filter to remove the DC from the signals
 */
public class Highpass {

    private float dc = 0;
    private float a = 0.05F;
    private boolean isActive = true;

    public void setAlpha(float alpha) {
        a = alpha;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean getIsActive() {return isActive;}

    public float filter(float v) {
        dc = a * v + (1 - a) * dc;
        if (isActive) {
            v = v - dc;
        }
        return v;
    }
}
