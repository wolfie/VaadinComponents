/* 
 * Copyright 2009 Henrik Paul
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.lightframe.components;

import org.lightframe.components.client.ui.VRefresher;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * A component that enables asynchronous UI changes invoked from external
 * {@link Thread Threads} to be rendered and refreshed in the client.
 * 
 * @author Henrik Paul
 */
public class Refresher extends AbstractComponent {
    private static final long serialVersionUID = -2818447361687554688L;

    private long refreshIntervalInMillis = -1;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        target.addAttribute("pollinginterval", refreshIntervalInMillis);
    }

    /**
     * Define a refresh interval.
     * 
     * @param intervalInMillis
     *            The desired refresh interval in milliseconds. An interval of
     *            zero or less temporarily inactivates the refresh.
     */
    public void setRefreshInterval(final long intervalInMillis) {
        refreshIntervalInMillis = intervalInMillis;
        requestRepaint();
    }

    /**
     * Get the currently used refreshing interval.
     * 
     * @return The refresh interval in milliseconds. A result of zero or less
     *         means that the refresher is currently inactive.
     */
    public long getRefreshInterval() {
        return refreshIntervalInMillis;
    }

    @Override
    public String getTag() {
        return VRefresher.TAGNAME;
    }
}
