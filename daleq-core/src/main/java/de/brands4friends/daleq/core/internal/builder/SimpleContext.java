/*
 * Copyright 2012 brands4friends, Private Sale GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.brands4friends.daleq.core.internal.builder;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import de.brands4friends.daleq.core.Context;
import de.brands4friends.daleq.core.internal.conversion.TypeConversion;
import de.brands4friends.daleq.core.internal.conversion.TypeConversionImpl;
import de.brands4friends.daleq.core.internal.template.TemplateValueFactory;
import de.brands4friends.daleq.core.internal.template.TemplateValueFactoryImpl;
import de.brands4friends.daleq.core.internal.types.CachingTableTypeRepository;
import de.brands4friends.daleq.core.internal.types.TableTypeRepository;

public class SimpleContext implements Context {

    private final TypeConversion typeConversion = new TypeConversionImpl();
    private final TemplateValueFactory templateValueFactory = TemplateValueFactoryImpl.getInstance();
    private final TableTypeRepository tableTypeRepository = new CachingTableTypeRepository();

    private final Map<Class<?>, Object> registry = ImmutableMap.of(
            TypeConversion.class, typeConversion,
            TemplateValueFactory.class, templateValueFactory,
            TableTypeRepository.class, tableTypeRepository
    );

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getService(final Class<T> serviceName) {
        Preconditions.checkNotNull(serviceName);
        final Object service = registry.get(serviceName);
        if (service == null) {
            throw new IllegalArgumentException("No service registered: " + serviceName.getCanonicalName());
        }
        if (!serviceName.isAssignableFrom(service.getClass())) {
            throw new IllegalStateException(
                    "Service " + service.getClass().getCanonicalName()
                            + " is not of type " + serviceName.getCanonicalName());
        }
        return (T) service;
    }
}
