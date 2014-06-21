/**
 * Copyright 2014 Alessandro Lacava
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.typesafely.util;

/**
 * This class is semantically the same as the {@link java.util.function.Supplier} class apart from the fact that
 * its {@link FailableSupplier#get()} method may throw an {@link java.lang.Exception}
 *
 * @author Alessandro Lacava
 * @since 2014-06-20
 */
@FunctionalInterface
public interface FailableSupplier<T> {

    /**
     *
     * @return a value of type {@code T}
     * @throws Exception
     */
    public T get() throws Exception;
}
